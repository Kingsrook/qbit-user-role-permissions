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

package com.kingsrook.qbits.userrolepermissions.model;


import java.time.Instant;
import java.util.List;
import com.kingsrook.qbits.userrolepermissions.customizers.RolePermissionIntCustomizer;
import com.kingsrook.qqq.backend.core.actions.customizers.TableCustomizers;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.data.QField;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.model.data.QRecordEntity;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.code.QCodeReference;
import com.kingsrook.qqq.backend.core.model.metadata.joins.QJoinMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QIcon;
import com.kingsrook.qqq.backend.core.model.metadata.producers.MetaDataCustomizerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.QMetaDataProducingEntity;
import com.kingsrook.qqq.backend.core.model.metadata.tables.ExposedJoin;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QFieldSection;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.tables.Tier;
import com.kingsrook.qqq.backend.core.model.metadata.tables.UniqueKey;


/*******************************************************************************
 ** QRecord Entity for RolePermissionInt table
 *******************************************************************************/
@QMetaDataProducingEntity(
   produceTableMetaData = true,
   tableMetaDataCustomizer = RolePermissionInt.MetaDataCustomizer.class
)
public class RolePermissionInt extends QRecordEntity
{
   public static final String TABLE_NAME = "rolePermissionInt";



   /***************************************************************************
    **
    ***************************************************************************/
   public static class MetaDataCustomizer implements MetaDataCustomizerInterface<QTableMetaData>
   {

      /***************************************************************************
       **
       ***************************************************************************/
      @Override
      public QTableMetaData customizeMetaData(QInstance qInstance, QTableMetaData table) throws QException
      {
         return table
            .withIcon(new QIcon().withName("lock_open"))
            .withRecordLabelFormat("%s - %s")
            .withUniqueKey(new UniqueKey("permissionId", "roleId"))
            .withRecordLabelFields("permissionId", "roleId")
            .withSection(new QFieldSection("identity", new QIcon().withName("badge"), Tier.T1, List.of("id", "permissionId", "roleId")))
            .withSection(new QFieldSection("dates", new QIcon().withName("calendar_month"), Tier.T3, List.of("createDate", "modifyDate")))
            .withExposedJoin(new ExposedJoin().withJoinTable(Permission.TABLE_NAME).withJoinPath(List.of(QJoinMetaData.makeInferredJoinName(Permission.TABLE_NAME, RolePermissionInt.TABLE_NAME))))
            .withExposedJoin(new ExposedJoin().withJoinTable(Role.TABLE_NAME).withJoinPath(List.of(QJoinMetaData.makeInferredJoinName(Role.TABLE_NAME, RolePermissionInt.TABLE_NAME))))
            .withCustomizer(TableCustomizers.POST_INSERT_RECORD, new QCodeReference(RolePermissionIntCustomizer.class))
            .withCustomizer(TableCustomizers.POST_UPDATE_RECORD, new QCodeReference(RolePermissionIntCustomizer.class))
            .withCustomizer(TableCustomizers.POST_DELETE_RECORD, new QCodeReference(RolePermissionIntCustomizer.class));
      }
   }



   @QField(isEditable = false, isPrimaryKey = true)
   private Integer id;

   @QField(isEditable = false)
   private Instant createDate;

   @QField(isEditable = false)
   private Instant modifyDate;

   @QField(isRequired = true, possibleValueSourceName = Role.TABLE_NAME)
   private Integer roleId;

   @QField(isRequired = true, possibleValueSourceName = Permission.TABLE_NAME)
   private Integer permissionId;



   /*******************************************************************************
    ** Default constructor
    *******************************************************************************/
   public RolePermissionInt()
   {
   }



   /*******************************************************************************
    ** Constructor that takes a QRecord
    *******************************************************************************/
   public RolePermissionInt(QRecord record)
   {
      populateFromQRecord(record);
   }



   /*******************************************************************************
    ** Getter for id
    *******************************************************************************/
   public Integer getId()
   {
      return (this.id);
   }



   /*******************************************************************************
    ** Setter for id
    *******************************************************************************/
   public void setId(Integer id)
   {
      this.id = id;
   }



   /*******************************************************************************
    ** Fluent setter for id
    *******************************************************************************/
   public RolePermissionInt withId(Integer id)
   {
      this.id = id;
      return (this);
   }



   /*******************************************************************************
    ** Getter for createDate
    *******************************************************************************/
   public Instant getCreateDate()
   {
      return (this.createDate);
   }



   /*******************************************************************************
    ** Setter for createDate
    *******************************************************************************/
   public void setCreateDate(Instant createDate)
   {
      this.createDate = createDate;
   }



   /*******************************************************************************
    ** Fluent setter for createDate
    *******************************************************************************/
   public RolePermissionInt withCreateDate(Instant createDate)
   {
      this.createDate = createDate;
      return (this);
   }



   /*******************************************************************************
    ** Getter for modifyDate
    *******************************************************************************/
   public Instant getModifyDate()
   {
      return (this.modifyDate);
   }



   /*******************************************************************************
    ** Setter for modifyDate
    *******************************************************************************/
   public void setModifyDate(Instant modifyDate)
   {
      this.modifyDate = modifyDate;
   }



   /*******************************************************************************
    ** Fluent setter for modifyDate
    *******************************************************************************/
   public RolePermissionInt withModifyDate(Instant modifyDate)
   {
      this.modifyDate = modifyDate;
      return (this);
   }



   /*******************************************************************************
    ** Getter for roleId
    *******************************************************************************/
   public Integer getRoleId()
   {
      return (this.roleId);
   }



   /*******************************************************************************
    ** Setter for roleId
    *******************************************************************************/
   public void setRoleId(Integer roleId)
   {
      this.roleId = roleId;
   }



   /*******************************************************************************
    ** Fluent setter for roleId
    *******************************************************************************/
   public RolePermissionInt withRoleId(Integer roleId)
   {
      this.roleId = roleId;
      return (this);
   }



   /*******************************************************************************
    ** Getter for permissionId
    *******************************************************************************/
   public Integer getPermissionId()
   {
      return (this.permissionId);
   }



   /*******************************************************************************
    ** Setter for permissionId
    *******************************************************************************/
   public void setPermissionId(Integer permissionId)
   {
      this.permissionId = permissionId;
   }



   /*******************************************************************************
    ** Fluent setter for permissionId
    *******************************************************************************/
   public RolePermissionInt withPermissionId(Integer permissionId)
   {
      this.permissionId = permissionId;
      return (this);
   }

}
