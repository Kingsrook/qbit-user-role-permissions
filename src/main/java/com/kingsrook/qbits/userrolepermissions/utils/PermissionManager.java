/*
 * QQQ - Low-code Application Framework for Engineers.
 * Copyright (C) 2021-2025.  Kingsrook, LLC
 * 651 N Broad St Ste 205 # 6917 | Middletown DE 19709 | United States
 * contact@kingsrook.com
 * https://github.com/Kingsrook/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.kingsrook.qbits.userrolepermissions.utils;


import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.kingsrook.qbits.userrolepermissions.model.Permission;
import com.kingsrook.qbits.userrolepermissions.model.Role;
import com.kingsrook.qbits.userrolepermissions.model.RolePermissionInt;
import com.kingsrook.qbits.userrolepermissions.model.UserPermissionInt;
import com.kingsrook.qbits.userrolepermissions.model.UserRoleInt;
import com.kingsrook.qqq.backend.core.actions.tables.QueryAction;
import com.kingsrook.qqq.backend.core.context.QContext;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.logging.QLogger;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QCriteriaOperator;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QQueryFilter;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QueryInput;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QueryJoin;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QueryOutput;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.joins.QJoinMetaData;
import com.kingsrook.qqq.backend.core.utils.CollectionUtils;
import com.kingsrook.qqq.backend.core.utils.memoization.Memoization;


/*******************************************************************************
 **
 *******************************************************************************/
public class PermissionManager
{
   private static final QLogger LOG = QLogger.getLogger(PermissionManager.class);

   private static PermissionManager singletonReference = null;

   private Memoization<Integer, Set<String>> getEffectivePermissionsForUserMemoization = new Memoization<Integer, Set<String>>()
      .withTimeout(Duration.ofMinutes(5));

   private Memoization<Set<Integer>, Set<String>> getEffectivePermissionsForRolesMemoization = new Memoization<Set<Integer>, Set<String>>()
      .withTimeout(Duration.ofMinutes(5));

   private Map<Integer, Set<Set<Integer>>> roleIdToRoleSetIds = Collections.synchronizedMap(new HashMap<>());


   /*******************************************************************************
    ** Singleton constructor
    *******************************************************************************/
   private PermissionManager()
   {

   }



   /*******************************************************************************
    ** Singleton accessor
    *******************************************************************************/
   public static PermissionManager getInstance()
   {
      if(singletonReference == null)
      {
         singletonReference = new PermissionManager();
      }
      return (singletonReference);
   }



   /***************************************************************************
    **
    ***************************************************************************/
   public void flushCacheForUpdatedUserIds(Collection<Integer> userIds)
   {
      for(Integer userId : CollectionUtils.nonNullCollection(userIds))
      {
         getEffectivePermissionsForUserMemoization.clearKey(userId);
      }
   }



   /***************************************************************************
    **
    ***************************************************************************/
   public void flushCacheForUpdatedRoleIds(Collection<Integer> roleIds)
   {
      if(CollectionUtils.nullSafeIsEmpty(roleIds))
      {
         return;
      }

      Set<Set<Integer>> roleIdsSetsToClear = new HashSet<>();
      for(Integer roleId : roleIds)
      {
         if(roleIdToRoleSetIds.containsKey(roleId))
         {
            roleIdsSetsToClear.addAll(roleIdToRoleSetIds.get(roleId));
         }
      }

      for(Set<Integer> roleIdsSet : roleIdsSetsToClear)
      {
         getEffectivePermissionsForRolesMemoization.clearKey(roleIdsSet);
      }
   }



   /***************************************************************************
    **
    ***************************************************************************/
   public void flushAllCache()
   {
      getEffectivePermissionsForUserMemoization.clear();
      getEffectivePermissionsForRolesMemoization.clear();
   }



   /***************************************************************************
    **
    ***************************************************************************/
   public Set<String> getEffectivePermissionsForUser(Integer userId) throws QException
   {
      if(userId == null)
      {
         return (Collections.emptySet());
      }

      return (getEffectivePermissionsForUserMemoization.getResultThrowing(userId, u ->
         doGetEffectivePermissionsForUser(u)))
         .orElseThrow(() -> new QException("Could not get effective permissions for user with id: " + userId));
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private Set<String> doGetEffectivePermissionsForUser(Integer userId) throws QException
   {
      QInstance qInstance = QContext.getQInstance();

      ////////////////////////////////
      // query for role permissions //
      ////////////////////////////////
      QueryOutput roleQueryOutput = new QueryAction().execute(new QueryInput(Permission.TABLE_NAME)
         .withQueryJoin(new QueryJoin(RolePermissionInt.TABLE_NAME)
            .withBaseTableOrAlias(Permission.TABLE_NAME)
            .withJoinMetaData(qInstance.getJoin(QJoinMetaData.makeInferredJoinName(Permission.TABLE_NAME, RolePermissionInt.TABLE_NAME))))

         .withQueryJoin(new QueryJoin(Role.TABLE_NAME)
            .withBaseTableOrAlias(RolePermissionInt.TABLE_NAME)
            .withJoinMetaData(qInstance.getJoin(QJoinMetaData.makeInferredJoinName(Role.TABLE_NAME, RolePermissionInt.TABLE_NAME))))

         .withQueryJoin(new QueryJoin(UserRoleInt.TABLE_NAME)
            .withBaseTableOrAlias(Role.TABLE_NAME)
            .withJoinMetaData(qInstance.getJoin(QJoinMetaData.makeInferredJoinName(Role.TABLE_NAME, UserRoleInt.TABLE_NAME))))

         .withFilter(new QQueryFilter().withCriteria(UserRoleInt.TABLE_NAME + ".userId", QCriteriaOperator.EQUALS, userId)));

      List<QRecord> rolePermissions     = roleQueryOutput.getRecords();
      Set<String>   permissionsFromRole = rolePermissions.stream().map(r -> r.getValueString("name")).collect(Collectors.toSet());

      ////////////////////////////////
      // query for user permissions //
      ////////////////////////////////
      QueryOutput userQueryOutput = new QueryAction().execute(new QueryInput(Permission.TABLE_NAME)
         .withFilter(new QQueryFilter().withCriteria(UserPermissionInt.TABLE_NAME + ".userId", QCriteriaOperator.EQUALS, userId)));
      List<QRecord> userPermissions     = userQueryOutput.getRecords();
      Set<String>   permissionsFromUser = userPermissions.stream().map(r -> r.getValueString("name")).collect(Collectors.toSet());

      //////////////////////
      // union and return //
      //////////////////////
      permissionsFromRole.addAll(permissionsFromUser);
      return (permissionsFromRole);
   }



   /***************************************************************************
    **
    ***************************************************************************/
   public Set<String> getEffectivePermissionsForRoles(Set<Integer> roleIds) throws QException
   {
      if(CollectionUtils.nullSafeIsEmpty(roleIds))
      {
         return (Collections.emptySet());
      }

      return (getEffectivePermissionsForRolesMemoization.getResultThrowing(roleIds, rs ->
         doGetEffectivePermissionsForRoles(rs)))
         .orElseThrow(() -> new QException("Could not get effective permissions for role ids: " + roleIds));
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private Set<String> doGetEffectivePermissionsForRoles(Set<Integer> roleIds) throws QException
   {
      for(Integer roleId : roleIds)
      {
         roleIdToRoleSetIds.computeIfAbsent(roleId, k -> new HashSet<>());
         roleIdToRoleSetIds.get(roleId).add(roleIds);
      }

      QInstance qInstance = QContext.getQInstance();

      ////////////////////////////////
      // query for role permissions //
      ////////////////////////////////
      QueryOutput roleQueryOutput = new QueryAction().execute(new QueryInput(Permission.TABLE_NAME)
         .withQueryJoin(new QueryJoin(RolePermissionInt.TABLE_NAME)
            .withBaseTableOrAlias(Permission.TABLE_NAME)
            .withJoinMetaData(qInstance.getJoin(QJoinMetaData.makeInferredJoinName(Permission.TABLE_NAME, RolePermissionInt.TABLE_NAME))))

         .withFilter(new QQueryFilter().withCriteria(RolePermissionInt.TABLE_NAME + ".roleId", QCriteriaOperator.IN, roleIds)));

      List<QRecord> rolePermissions     = roleQueryOutput.getRecords();
      Set<String>   permissionsFromRole = rolePermissions.stream().map(r -> r.getValueString("name")).collect(Collectors.toSet());

      //////////////////////
      // union and return //
      //////////////////////
      return (permissionsFromRole);
   }
}
