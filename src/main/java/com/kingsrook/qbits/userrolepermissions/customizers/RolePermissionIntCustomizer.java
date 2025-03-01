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

package com.kingsrook.qbits.userrolepermissions.customizers;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.kingsrook.qbits.userrolepermissions.model.UserRoleInt;
import com.kingsrook.qbits.userrolepermissions.utils.PermissionManager;
import com.kingsrook.qqq.backend.core.actions.customizers.TableCustomizerInterface;
import com.kingsrook.qqq.backend.core.actions.tables.QueryAction;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.actions.tables.delete.DeleteInput;
import com.kingsrook.qqq.backend.core.model.actions.tables.insert.InsertInput;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QCriteriaOperator;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QFilterCriteria;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QQueryFilter;
import com.kingsrook.qqq.backend.core.model.actions.tables.update.UpdateInput;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.utils.CollectionUtils;


/*******************************************************************************
 **
 *******************************************************************************/
public class RolePermissionIntCustomizer implements TableCustomizerInterface
{
   private Set<Integer> userIdsPreUpdate;



   /***************************************************************************
    **
    ***************************************************************************/
   @Override
   public List<QRecord> postInsert(InsertInput insertInput, List<QRecord> records) throws QException
   {
      Set<Integer> userIds = getUserIdsForRolePermissionIntRecords(records);

      PermissionManager.getInstance().flushCacheForUpdatedUserIds(userIds);
      return (records);
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private static Set<Integer> getUserIdsForRolePermissionIntRecords(List<QRecord> records) throws QException
   {
      Set<Integer>  roleIds      = CollectionUtils.nonNullList(records).stream().map(r -> r.getValueInteger("roleId")).collect(Collectors.toSet());
      List<QRecord> userRoleInts = QueryAction.execute(UserRoleInt.TABLE_NAME, new QQueryFilter(new QFilterCriteria("roleId", QCriteriaOperator.IN, roleIds)));
      Set<Integer>  userIds      = userRoleInts.stream().map(r -> r.getValueInteger("userId")).collect(Collectors.toSet());

      return userIds;
   }



   /***************************************************************************
    **
    ***************************************************************************/
   @Override
   public List<QRecord> preUpdate(UpdateInput updateInput, List<QRecord> records, boolean isPreview, Optional<List<QRecord>> oldRecordList) throws QException
   {
      if(oldRecordList.isPresent())
      {
         this.userIdsPreUpdate = getUserIdsForRolePermissionIntRecords(oldRecordList.get());
      }

      return (records);
   }



   /***************************************************************************
    **
    ***************************************************************************/
   @Override
   public List<QRecord> postUpdate(UpdateInput updateInput, List<QRecord> records, Optional<List<QRecord>> oldRecordList) throws QException
   {
      Set<Integer> userIds = getUserIdsForRolePermissionIntRecords(records);
      if(userIdsPreUpdate != null)
      {
         userIds = new HashSet<>(userIds);
         userIds.addAll(userIdsPreUpdate);
      }

      PermissionManager.getInstance().flushCacheForUpdatedUserIds(userIds);

      return (records);
   }



   /***************************************************************************
    **
    ***************************************************************************/
   @Override
   public List<QRecord> postDelete(DeleteInput deleteInput, List<QRecord> records) throws QException
   {
      Set<Integer> userIds = getUserIdsForRolePermissionIntRecords(records);

      PermissionManager.getInstance().flushCacheForUpdatedUserIds(userIds);
      return (records);
   }

}
