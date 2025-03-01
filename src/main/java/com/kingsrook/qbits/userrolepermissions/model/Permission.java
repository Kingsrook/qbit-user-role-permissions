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
import com.kingsrook.qqq.backend.core.model.metadata.tables.QFieldSection;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.tables.Tier;
import com.kingsrook.qqq.backend.core.model.metadata.tables.UniqueKey;


/*******************************************************************************
 ** QRecord Entity for Permission table
 *******************************************************************************/
@QMetaDataProducingEntity(
   produceTableMetaData = true,
   tableMetaDataCustomizer = Permission.MetaDataCustomizer.class,

   producePossibleValueSource = true,

   childTables =
   {
      @ChildTable(childTableEntityClass = RolePermissionInt.class, joinFieldName = "permissionId",
         childJoin = @ChildJoin(enabled = true),
         childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Roles", canAddChildRecords = true)),
      @ChildTable(childTableEntityClass = UserPermissionInt.class, joinFieldName = "permissionId",
         childJoin = @ChildJoin(enabled = true),
         childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Users", canAddChildRecords = true))
   })
public class Permission extends QRecordEntity
{
   public static final String TABLE_NAME = "permission";

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
            .withIcon(new QIcon().withName("lock"))
            .withRecordLabelFields("name")
            .withUniqueKey(new UniqueKey("name"))
            .withSection(new QFieldSection("identity", new QIcon().withName("badge"), Tier.T1, List.of("id", "name", "objectType", "objectLabel", "description")))
            .withSection(new QFieldSection("roles", new QIcon().withName("diversity_3"), Tier.T2).withWidgetName(QJoinMetaData.makeInferredJoinName(Permission.TABLE_NAME, RolePermissionInt.TABLE_NAME)))
            .withSection(new QFieldSection("users", new QIcon().withName("user"), Tier.T2).withWidgetName(QJoinMetaData.makeInferredJoinName(Permission.TABLE_NAME, UserPermissionInt.TABLE_NAME)))
            .withSection(new QFieldSection("dates", new QIcon().withName("calendar_month"), Tier.T3, List.of("createDate", "modifyDate")));
      }
   }

   @QField(isEditable = false, isPrimaryKey = true)
   private Integer id;

   @QField(isEditable = false)
   private Instant createDate;

   @QField(isEditable = false)
   private Instant modifyDate;

   @QField(maxLength = 250, valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String name;

   @QField(maxLength = 500, valueTooLongBehavior = ValueTooLongBehavior.TRUNCATE_ELLIPSIS)
   private String description;

   @QField(maxLength = 50, valueTooLongBehavior = ValueTooLongBehavior.ERROR, possibleValueSourceName = PermissionObjectType.NAME)
   private String objectType;

   @QField(maxLength = 250, valueTooLongBehavior = ValueTooLongBehavior.TRUNCATE_ELLIPSIS)
   private String objectLabel;


   /*******************************************************************************
    ** Default constructor
    *******************************************************************************/
   public Permission()
   {
   }



   /*******************************************************************************
    ** Constructor that takes a QRecord
    *******************************************************************************/
   public Permission(QRecord record)
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
   public Permission withId(Integer id)
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
   public Permission withCreateDate(Instant createDate)
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
   public Permission withModifyDate(Instant modifyDate)
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
   public Permission withName(String name)
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
   public Permission withDescription(String description)
   {
      this.description = description;
      return (this);
   }



   /*******************************************************************************
    ** Getter for objectType
    *******************************************************************************/
   public String getObjectType()
   {
      return (this.objectType);
   }



   /*******************************************************************************
    ** Setter for objectType
    *******************************************************************************/
   public void setObjectType(String objectType)
   {
      this.objectType = objectType;
   }



   /*******************************************************************************
    ** Fluent setter for objectType
    *******************************************************************************/
   public Permission withObjectType(String objectType)
   {
      this.objectType = objectType;
      return (this);
   }



   /*******************************************************************************
    ** Getter for objectLabel
    *******************************************************************************/
   public String getObjectLabel()
   {
      return (this.objectLabel);
   }



   /*******************************************************************************
    ** Setter for objectLabel
    *******************************************************************************/
   public void setObjectLabel(String objectLabel)
   {
      this.objectLabel = objectLabel;
   }



   /*******************************************************************************
    ** Fluent setter for objectLabel
    *******************************************************************************/
   public Permission withObjectLabel(String objectLabel)
   {
      this.objectLabel = objectLabel;
      return (this);
   }


}
