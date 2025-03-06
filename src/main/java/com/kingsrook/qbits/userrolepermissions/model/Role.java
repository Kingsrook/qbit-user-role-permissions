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
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.data.QField;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.model.data.QRecordEntity;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.fields.ValueTooLongBehavior;
import com.kingsrook.qqq.backend.core.model.metadata.joins.QJoinMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QIcon;
import com.kingsrook.qqq.backend.core.model.metadata.producers.MetaDataCustomizerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildJoin;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildRecordListWidget;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildTable;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.QMetaDataProducingEntity;
import com.kingsrook.qqq.backend.core.model.metadata.tables.ExposedJoin;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QFieldSection;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.tables.Tier;
import com.kingsrook.qqq.backend.core.model.metadata.tables.UniqueKey;


/*******************************************************************************
 ** QRecord Entity for Role table
 *******************************************************************************/
@QMetaDataProducingEntity(
   produceTableMetaData = true,
   tableMetaDataCustomizer = Role.MetaDataCustomizer.class,

   producePossibleValueSource = true,

   childTables =
      {
         @ChildTable(childTableEntityClass = RolePermissionInt.class, joinFieldName = "roleId",
            childJoin = @ChildJoin(enabled = true),
            childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Permissions", canAddChildRecords = true)),

         @ChildTable(childTableEntityClass = UserRoleInt.class, joinFieldName = "roleId",
            childJoin = @ChildJoin(enabled = true),
            childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Users", canAddChildRecords = true)),
      })
public class Role extends QRecordEntity
{
   public static final String TABLE_NAME = "role";



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
            .withIcon(new QIcon().withName("diversity_3"))
            .withRecordLabelFields("name")
            .withUniqueKey(new UniqueKey("name"))
            .withSection(new QFieldSection("identity", new QIcon().withName("badge"), Tier.T1, List.of("id", "name", "description")))
            .withSection(new QFieldSection("users", new QIcon().withName("person"), Tier.T2).withWidgetName(QJoinMetaData.makeInferredJoinName(Role.TABLE_NAME, UserRoleInt.TABLE_NAME)))
            .withSection(new QFieldSection("permissions", new QIcon().withName("key"), Tier.T2).withWidgetName(QJoinMetaData.makeInferredJoinName(Role.TABLE_NAME, RolePermissionInt.TABLE_NAME)))
            .withSection(new QFieldSection("dates", new QIcon().withName("calendar_month"), Tier.T3, List.of("createDate", "modifyDate")))
            .withExposedJoin(new ExposedJoin().withJoinTable(Permission.TABLE_NAME).withJoinPath(List.of(
               QJoinMetaData.makeInferredJoinName(Role.TABLE_NAME, RolePermissionInt.TABLE_NAME),
               QJoinMetaData.makeInferredJoinName(Permission.TABLE_NAME, RolePermissionInt.TABLE_NAME)
            )))
            .withExposedJoin(new ExposedJoin().withJoinTable(User.TABLE_NAME).withJoinPath(List.of(
               QJoinMetaData.makeInferredJoinName(Role.TABLE_NAME, UserRoleInt.TABLE_NAME),
               QJoinMetaData.makeInferredJoinName(User.TABLE_NAME, UserRoleInt.TABLE_NAME)
            )));
      }
   }


   @QField(isEditable = false, isPrimaryKey = true)
   private Integer id;

   @QField(isEditable = false)
   private Instant createDate;

   @QField(isEditable = false)
   private Instant modifyDate;

   @QField(isRequired = true, maxLength = 100, valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String name;

   @QField(maxLength = 500, valueTooLongBehavior = ValueTooLongBehavior.TRUNCATE_ELLIPSIS)
   private String description;



   /*******************************************************************************
    ** Default constructor
    *******************************************************************************/
   public Role()
   {
   }



   /*******************************************************************************
    ** Constructor that takes a QRecord
    *******************************************************************************/
   public Role(QRecord record)
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
   public Role withId(Integer id)
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
   public Role withCreateDate(Instant createDate)
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
   public Role withModifyDate(Instant modifyDate)
   {
      this.modifyDate = modifyDate;
      return (this);
   }



   /*******************************************************************************
    ** Getter for name
    *******************************************************************************/
   public String getName()
   {
      return (this.name);
   }



   /*******************************************************************************
    ** Setter for name
    *******************************************************************************/
   public void setName(String name)
   {
      this.name = name;
   }



   /*******************************************************************************
    ** Fluent setter for name
    *******************************************************************************/
   public Role withName(String name)
   {
      this.name = name;
      return (this);
   }



   /*******************************************************************************
    ** Getter for description
    *******************************************************************************/
   public String getDescription()
   {
      return (this.description);
   }



   /*******************************************************************************
    ** Setter for description
    *******************************************************************************/
   public void setDescription(String description)
   {
      this.description = description;
   }



   /*******************************************************************************
    ** Fluent setter for description
    *******************************************************************************/
   public Role withDescription(String description)
   {
      this.description = description;
      return (this);
   }

}
