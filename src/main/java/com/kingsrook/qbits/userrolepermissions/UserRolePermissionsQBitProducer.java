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


import java.util.Iterator;
import java.util.List;
import com.kingsrook.qbits.userrolepermissions.model.User;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.metadata.MetaDataProducerHelper;
import com.kingsrook.qqq.backend.core.model.metadata.MetaDataProducerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.producers.PossibleValueSourceOfTableGenericMetaDataProducer;
import com.kingsrook.qqq.backend.core.model.metadata.producers.RecordEntityToTableGenericMetaDataProducer;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.QBitMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.QBitProducer;


/*******************************************************************************
 **
 *******************************************************************************/
public class UserRolePermissionsQBitProducer implements QBitProducer
{
   private UserRolePermissionsQBitConfig userRolePermissionsQBitConfig;



   /***************************************************************************
    **
    ***************************************************************************/
   @Override
   public void produce(QInstance qInstance, String namespace) throws QException
   {
      QBitMetaData qBitMetaData = new QBitMetaData()
         .withGroupId("com.kingsrook.qbits")
         .withArtifactId("user-role-permissions")
         .withVersion("0.1.2")
         .withNamespace(namespace)
         .withConfig(userRolePermissionsQBitConfig);
      qInstance.addQBit(qBitMetaData);

      List<MetaDataProducerInterface<?>> producers = MetaDataProducerHelper.findProducers(getClass().getPackageName());

      ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      // if the user-table is not to be provided by this qbit, then remove any meta-data producers that came from that entity class //
      ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      if(userRolePermissionsQBitConfig.getUserTableConfig() != null && !userRolePermissionsQBitConfig.getUserTableConfig().getDoProvideTable())
      {
         Iterator<MetaDataProducerInterface<?>> iterator = producers.iterator();
         while(iterator.hasNext())
         {
            MetaDataProducerInterface<?> producer = iterator.next();
            if(producer.getSourceClass().equals(User.class))
            {
               if(producer.getClass().equals(RecordEntityToTableGenericMetaDataProducer.class)
                  || producer.getClass().equals(PossibleValueSourceOfTableGenericMetaDataProducer.class))
               {
                  iterator.remove();
               }
            }
         }
      }

      finishProducing(qInstance, qBitMetaData, userRolePermissionsQBitConfig, producers);
   }



   /*******************************************************************************
    ** Getter for userRolePermissionsQBitConfig
    *******************************************************************************/
   public UserRolePermissionsQBitConfig getUserRolePermissionsQBitConfig()
   {
      return (this.userRolePermissionsQBitConfig);
   }



   /*******************************************************************************
    ** Setter for userRolePermissionsQBitConfig
    *******************************************************************************/
   public void setUserRolePermissionsQBitConfig(UserRolePermissionsQBitConfig userRolePermissionsQBitConfig)
   {
      this.userRolePermissionsQBitConfig = userRolePermissionsQBitConfig;
   }



   /*******************************************************************************
    ** Fluent setter for userRolePermissionsQBitConfig
    *******************************************************************************/
   public UserRolePermissionsQBitProducer withUserRolePermissionsQBitConfig(UserRolePermissionsQBitConfig userRolePermissionsQBitConfig)
   {
      this.userRolePermissionsQBitConfig = userRolePermissionsQBitConfig;
      return (this);
   }

}
