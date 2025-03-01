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
 ** QRecord Entity for User table
 *******************************************************************************/
@QMetaDataProducingEntity(
   producePossibleValueSource = true,
   produceTableMetaData = true,
   childTables =
      {
         @ChildTable(childTableEntityClass = UserPermissionInt.class, joinFieldName = "userId",
            childJoin = @ChildJoin(enabled = true),
            childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Permissions", canAddChildRecords = true)),

         @ChildTable(childTableEntityClass = UserRoleInt.class, joinFieldName = "userId",
            childJoin = @ChildJoin(enabled = true),
            childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Roles", canAddChildRecords = true)),
      })
public class User extends QRecordEntity
{
   public static final String TABLE_NAME = "user";



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
            .withIcon(new QIcon().withName("person_outline"))
            .withRecordLabelFields("fullName")
            .withUniqueKey(new UniqueKey("email"))
            .withSection(new QFieldSection("identity", new QIcon().withName("badge"), Tier.T1, List.of("id", "email", "fullName")))
            .withSection(new QFieldSection("roles", new QIcon().withName("diversity_3"), Tier.T2).withWidgetName(QJoinMetaData.makeInferredJoinName(User.TABLE_NAME, UserRoleInt.TABLE_NAME)))
            .withSection(new QFieldSection("permissions", new QIcon().withName("key"), Tier.T2).withWidgetName(QJoinMetaData.makeInferredJoinName(User.TABLE_NAME, UserPermissionInt.TABLE_NAME)))
            .withSection(new QFieldSection("dates", new QIcon().withName("calendar_month"), Tier.T3, List.of("createDate", "modifyDate")));
      }

   }



   @QField(isEditable = false, isPrimaryKey = true)
   private Integer id;

   @QField(isEditable = false)
   private Instant createDate;

   @QField(isEditable = false)
   private Instant modifyDate;

   @QField(isRequired = true, maxLength = 200, valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String email;

   @QField(isRequired = true, maxLength = 100, valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String fullName;



   /*******************************************************************************
    ** Default constructor
    *******************************************************************************/
   public User()
   {
   }



   /*******************************************************************************
    ** Constructor that takes a QRecord
    *******************************************************************************/
   public User(QRecord record)
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
   public User withId(Integer id)
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
   public User withCreateDate(Instant createDate)
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
   public User withModifyDate(Instant modifyDate)
   {
      this.modifyDate = modifyDate;
      return (this);
   }



   /*******************************************************************************
    ** Getter for email
    *******************************************************************************/
   public String getEmail()
   {
      return (this.email);
   }



   /*******************************************************************************
    ** Setter for email
    *******************************************************************************/
   public void setEmail(String email)
   {
      this.email = email;
   }



   /*******************************************************************************
    ** Fluent setter for email
    *******************************************************************************/
   public User withEmail(String email)
   {
      this.email = email;
      return (this);
   }



   /*******************************************************************************
    ** Getter for fullName
    *******************************************************************************/
   public String getFullName()
   {
      return (this.fullName);
   }



   /*******************************************************************************
    ** Setter for fullName
    *******************************************************************************/
   public void setFullName(String fullName)
   {
      this.fullName = fullName;
   }



   /*******************************************************************************
    ** Fluent setter for fullName
    *******************************************************************************/
   public User withFullName(String fullName)
   {
      this.fullName = fullName;
      return (this);
   }

}
