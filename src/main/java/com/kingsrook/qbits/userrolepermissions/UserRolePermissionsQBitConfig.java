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

package com.kingsrook.qbits.userrolepermissions;


import java.util.List;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.producers.MetaDataCustomizerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.ProvidedOrSuppliedTableConfig;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.QBitConfig;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;


/*******************************************************************************
 ** Configuration data for this qbit.
 **
 *******************************************************************************/
public class UserRolePermissionsQBitConfig implements QBitConfig
{
   private ProvidedOrSuppliedTableConfig               userTableConfig;
   private MetaDataCustomizerInterface<QTableMetaData> tableMetaDataCustomizer;



   /***************************************************************************
    **
    ***************************************************************************/
   @Override
   public void validate(QInstance qInstance, List<String> errors)
   {
      assertCondition(userTableConfig != null, "userTableConfig must be provided", errors);
   }



   /*******************************************************************************
    ** Getter for userTableConfig
    *******************************************************************************/
   public ProvidedOrSuppliedTableConfig getUserTableConfig()
   {
      return (this.userTableConfig);
   }



   /*******************************************************************************
    ** Setter for userTableConfig
    *******************************************************************************/
   public void setUserTableConfig(ProvidedOrSuppliedTableConfig userTableConfig)
   {
      this.userTableConfig = userTableConfig;
   }



   /*******************************************************************************
    ** Fluent setter for userTableConfig
    *******************************************************************************/
   public UserRolePermissionsQBitConfig withUserTableConfig(ProvidedOrSuppliedTableConfig userTableConfig)
   {
      this.userTableConfig = userTableConfig;
      return (this);
   }


   /*******************************************************************************
    ** Getter for tableMetaDataCustomizer
    *******************************************************************************/
   public MetaDataCustomizerInterface<QTableMetaData> getTableMetaDataCustomizer()
   {
      return (this.tableMetaDataCustomizer);
   }



   /*******************************************************************************
    ** Setter for tableMetaDataCustomizer
    *******************************************************************************/
   public void setTableMetaDataCustomizer(MetaDataCustomizerInterface<QTableMetaData> tableMetaDataCustomizer)
   {
      this.tableMetaDataCustomizer = tableMetaDataCustomizer;
   }



   /*******************************************************************************
    ** Fluent setter for tableMetaDataCustomizer
    *******************************************************************************/
   public UserRolePermissionsQBitConfig withTableMetaDataCustomizer(MetaDataCustomizerInterface<QTableMetaData> tableMetaDataCustomizer)
   {
      this.tableMetaDataCustomizer = tableMetaDataCustomizer;
      return (this);
   }


}
