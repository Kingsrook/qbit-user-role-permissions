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


import java.util.List;
import java.util.Map;
import java.util.Set;
import com.kingsrook.qbits.userrolepermissions.BaseTest;
import com.kingsrook.qbits.userrolepermissions.model.Permission;
import com.kingsrook.qbits.userrolepermissions.model.Role;
import com.kingsrook.qbits.userrolepermissions.model.RolePermissionInt;
import com.kingsrook.qbits.userrolepermissions.model.User;
import com.kingsrook.qbits.userrolepermissions.model.UserPermissionInt;
import com.kingsrook.qbits.userrolepermissions.model.UserRoleInt;
import com.kingsrook.qqq.backend.core.actions.tables.DeleteAction;
import com.kingsrook.qqq.backend.core.actions.tables.InsertAction;
import com.kingsrook.qqq.backend.core.actions.tables.UpdateAction;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.actions.tables.delete.DeleteInput;
import com.kingsrook.qqq.backend.core.model.actions.tables.insert.InsertInput;
import com.kingsrook.qqq.backend.core.model.actions.tables.update.UpdateInput;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.utils.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


/*******************************************************************************
 ** Unit test for PermissionsUtils 
 *******************************************************************************/
class PermissionManagerTest extends BaseTest
{
   private PermissionManager permissionManager = PermissionManager.getInstance();



   /*******************************************************************************
    **
    *******************************************************************************/
   @BeforeEach
   void beforeEach()
   {
      permissionManager.flushAllCache();
   }



   /*******************************************************************************
    ** rely on all the flushing in UserPermissionIntCustomizer
    *******************************************************************************/
   @Test
   void testUserPermissionInt() throws QException
   {
      Map<String, Integer> permissionMap = insertPermissions();
      Integer              userId1       = insertUser("test1");
      Integer              userId2       = insertUser("test2");

      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId1));
      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId2));

      Integer userPermissionIntId = insertUserPermissionInt(userId1, permissionMap.get("a"));

      assertEquals(Set.of("a"), permissionManager.getEffectivePermissionsForUser(userId1));

      new UpdateAction().execute(new UpdateInput(UserPermissionInt.TABLE_NAME).withRecord(new UserPermissionInt()
         .withId(userPermissionIntId).withPermissionId(permissionMap.get("b")).toQRecordOnlyChangedFields(true)));
      assertEquals(Set.of("b"), permissionManager.getEffectivePermissionsForUser(userId1));

      new UpdateAction().execute(new UpdateInput(UserPermissionInt.TABLE_NAME).withRecord(new UserPermissionInt()
         .withId(userPermissionIntId).withUserId(userId2).toQRecordOnlyChangedFields(true)));
      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId1));
      assertEquals(Set.of("b"), permissionManager.getEffectivePermissionsForUser(userId2));

      new UpdateAction().execute(new UpdateInput(UserPermissionInt.TABLE_NAME).withRecord(new UserPermissionInt()
         .withId(userPermissionIntId).withUserId(userId1).toQRecordOnlyChangedFields(true)));
      assertEquals(Set.of("b"), permissionManager.getEffectivePermissionsForUser(userId1));
      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId2));

      new DeleteAction().execute(new DeleteInput(UserPermissionInt.TABLE_NAME).withPrimaryKey(userPermissionIntId));
      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId1));

      insertUserPermissionInt(userId1, permissionMap.get("a"));
      insertUserPermissionInt(userId1, permissionMap.get("b"));
      insertUserPermissionInt(userId1, permissionMap.get("c"));
      assertEquals(Set.of("a", "b", "c"), permissionManager.getEffectivePermissionsForUser(userId1));
   }



   /*******************************************************************************
    ** relies on cache flushes from RolePermissionIntCustomizer
    *******************************************************************************/
   @Test
   void testAddingUserRoleIntThenRolePermissionIntThenDeletingRolePermissionInt() throws QException
   {
      Map<String, Integer> permissionMap = insertPermissions();
      Integer              userId        = insertUser("test1");

      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId));

      Integer roleId              = insertRole("Test 1");
      Integer userRoleIntId       = insertUserRoleInt(userId, roleId);
      Integer rolePermissionIntId = insertRolePermissionInt(roleId, permissionMap.get("b"));

      assertEquals(Set.of("b"), permissionManager.getEffectivePermissionsForUser(userId));

      new DeleteAction().execute(new DeleteInput(RolePermissionInt.TABLE_NAME).withPrimaryKey(rolePermissionIntId));
      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId));
   }



   /*******************************************************************************
    ** relies on cache flushes from UserRoleIntCustomizer
    *******************************************************************************/
   @Test
   void testAddingRolePermissionIntThenUserRoleIntThenDeletingUserRoleInt() throws QException
   {
      Map<String, Integer> permissionMap = insertPermissions();
      Integer              userId        = insertUser("test1");

      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId));

      Integer roleId              = insertRole("Test 1");
      Integer rolePermissionIntId = insertRolePermissionInt(roleId, permissionMap.get("c"));
      Integer userRoleIntId       = insertUserRoleInt(userId, roleId);

      assertEquals(Set.of("c"), permissionManager.getEffectivePermissionsForUser(userId));

      new DeleteAction().execute(new DeleteInput(UserRoleInt.TABLE_NAME).withPrimaryKey(userRoleIntId));
      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId));
   }



   /*******************************************************************************
    ** relies on cache flushes from UserRoleIntCustomizer
    *******************************************************************************/
   @Test
   void testUpdatesToUserRoleInt() throws QException
   {
      Map<String, Integer> permissionMap = insertPermissions();
      Integer              userId1       = insertUser("test1");
      Integer              userId2       = insertUser("test2");

      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId1));
      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId2));

      Integer roleIdA              = insertRole("Test A");
      Integer rolePermissionIntIdA = insertRolePermissionInt(roleIdA, permissionMap.get("a"));
      Integer userRoleIntId        = insertUserRoleInt(userId1, roleIdA);

      Integer roleIdB              = insertRole("Test B");
      Integer rolePermissionIntIdB = insertRolePermissionInt(roleIdB, permissionMap.get("b"));
      assertEquals(Set.of("a"), permissionManager.getEffectivePermissionsForUser(userId1));

      /////////////////////////////////////////
      // move the user from role A to role B //
      /////////////////////////////////////////
      new UpdateAction().execute(new UpdateInput(UserRoleInt.TABLE_NAME).withRecord(new UserRoleInt()
         .withId(userRoleIntId).withRoleId(roleIdB).toQRecordOnlyChangedFields(true)));
      assertEquals(Set.of("b"), permissionManager.getEffectivePermissionsForUser(userId1));

      /////////////////////////////////////////////////////////////////
      // switch the user-role-int to apply to user1 instead of user2 //
      /////////////////////////////////////////////////////////////////
      new UpdateAction().execute(new UpdateInput(UserRoleInt.TABLE_NAME).withRecord(new UserRoleInt()
         .withId(userRoleIntId).withUserId(userId2).toQRecordOnlyChangedFields(true)));
      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId1));
      assertEquals(Set.of("b"), permissionManager.getEffectivePermissionsForUser(userId2));
   }



   /*******************************************************************************
    **
    *******************************************************************************/
   @Test
   void testUpdatesToRolePermissionInt() throws QException
   {
      Map<String, Integer> permissionMap = insertPermissions();
      Integer              userId1       = insertUser("test1");
      Integer              userId2       = insertUser("test2");
      Integer              roleIdA       = insertRole("Test A");
      Integer              roleIdB       = insertRole("Test B");

      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId1));
      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId2));

      insertUserRoleInt(userId1, roleIdA);
      insertUserRoleInt(userId2, roleIdB);
      Integer rolePermissionIntId = insertRolePermissionInt(roleIdA, permissionMap.get("a"));
      assertEquals(Set.of("a"), permissionManager.getEffectivePermissionsForUser(userId1));
      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId2));

      //////////////////////////////////////////////
      // switch the int to a different permission //
      //////////////////////////////////////////////
      new UpdateAction().execute(new UpdateInput(RolePermissionInt.TABLE_NAME).withRecord(new RolePermissionInt()
         .withId(rolePermissionIntId).withPermissionId(permissionMap.get("b")).toQRecordOnlyChangedFields(true)));
      assertEquals(Set.of("b"), permissionManager.getEffectivePermissionsForUser(userId1));
      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId2));

      ////////////////////////////////////////////////////////////////////////
      // move the permission to a different role - our user1 should lose it //
      ////////////////////////////////////////////////////////////////////////
      new UpdateAction().execute(new UpdateInput(RolePermissionInt.TABLE_NAME).withRecord(new RolePermissionInt()
         .withId(rolePermissionIntId).withRoleId(roleIdB).toQRecordOnlyChangedFields(true)));
      assertEquals(Set.of(), permissionManager.getEffectivePermissionsForUser(userId1));
      assertEquals(Set.of("b"), permissionManager.getEffectivePermissionsForUser(userId2));
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private static Integer insertUserRoleInt(Integer userId, Integer roleId) throws QException
   {
      QRecord userRoleInt = new InsertAction().executeForRecord(new InsertInput(UserRoleInt.TABLE_NAME).withRecordEntity(new UserRoleInt().withUserId(userId).withRoleId(roleId)));
      Integer id          = userRoleInt.getValueInteger("id");
      return id;
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private static Integer insertRolePermissionInt(Integer roleId, Integer permissionId) throws QException
   {
      QRecord rolePermissionInt = new InsertAction().executeForRecord(new InsertInput(RolePermissionInt.TABLE_NAME).withRecordEntity(new RolePermissionInt().withRoleId(roleId).withPermissionId(permissionId)));
      Integer id                = rolePermissionInt.getValueInteger("id");
      return id;
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private static Map<String, Integer> insertPermissions() throws QException
   {
      List<QRecord> permissionRecords = new InsertAction().execute(new InsertInput(Permission.TABLE_NAME).withRecordEntities(List.of(
         new Permission().withName("a"),
         new Permission().withName("b"),
         new Permission().withName("c")
      ))).getRecords();
      Map<String, Integer> permissionMap = CollectionUtils.listToMap(permissionRecords, r -> r.getValueString("name"), r -> r.getValueInteger("id"));
      return permissionMap;
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private static Integer insertUser(String emailPrefix) throws QException
   {
      QRecord user   = new InsertAction().executeForRecord(new InsertInput(User.TABLE_NAME).withRecordEntity(new User().withEmail(emailPrefix + "@test.com").withFullName("Test")));
      Integer userId = user.getValueInteger("id");
      return userId;
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private static Integer insertRole(String name) throws QException
   {
      QRecord role   = new InsertAction().executeForRecord(new InsertInput(Role.TABLE_NAME).withRecordEntity(new Role().withName(name)));
      Integer roleId = role.getValueInteger("id");
      return roleId;
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private static Integer insertUserPermissionInt(Integer userId, Integer permissionId) throws QException
   {
      QRecord userPermissionInt   = new InsertAction().executeForRecord(new InsertInput(UserPermissionInt.TABLE_NAME).withRecordEntity(new UserPermissionInt().withUserId(userId).withPermissionId(permissionId)));
      Integer userPermissionIntId = userPermissionInt.getValueInteger("id");
      return userPermissionIntId;
   }

}