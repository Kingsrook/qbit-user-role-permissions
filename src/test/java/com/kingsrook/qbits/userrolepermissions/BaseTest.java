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


import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.kingsrook.qqq.backend.core.context.QContext;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.instances.QInstanceEnricher;
import com.kingsrook.qqq.backend.core.instances.QInstanceValidator;
import com.kingsrook.qqq.backend.core.model.metadata.QAuthenticationType;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.audits.AuditLevel;
import com.kingsrook.qqq.backend.core.model.metadata.audits.QAuditRules;
import com.kingsrook.qqq.backend.core.model.metadata.authentication.QAuthenticationMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.ProvidedOrSuppliedTableConfig;
import com.kingsrook.qqq.backend.core.model.session.QSession;
import com.kingsrook.qqq.backend.core.modules.backend.implementations.memory.MemoryRecordStore;
import com.kingsrook.qqq.backend.module.rdbms.jdbc.ConnectionManager;
import com.kingsrook.qqq.backend.module.rdbms.jdbc.QueryManager;
import com.kingsrook.qqq.backend.module.rdbms.model.metadata.RDBMSBackendMetaData;
import com.kingsrook.qqq.backend.module.rdbms.model.metadata.RDBMSTableBackendDetails;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;


/*******************************************************************************
 **
 *******************************************************************************/
public class BaseTest
{
   public static final String RDBMS_BACKEND_NAME = "h2rdbms";



   /*******************************************************************************
    **
    *******************************************************************************/
   @BeforeEach
   void baseBeforeEach() throws Exception
   {
      QInstance qInstance = defineQInstance();
      new QInstanceValidator().validate(qInstance);
      QContext.init(qInstance, new QSession());

      primeDatabase();

      MemoryRecordStore.fullReset();
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private void primeDatabase() throws SQLException
   {
      try(Connection connection = ConnectionManager.getConnection((RDBMSBackendMetaData) QContext.getQInstance().getBackend(RDBMS_BACKEND_NAME)))
      {
         InputStream  primeTestDatabaseSqlStream = BaseTest.class.getResourceAsStream("/test-database.sql");
         List<String> lines                      = IOUtils.readLines(primeTestDatabaseSqlStream, StandardCharsets.UTF_8);
         lines = lines.stream().filter(line -> !line.startsWith("-- ")).toList();
         String joinedSQL = String.join("\n", lines);
         for(String sql : joinedSQL.split(";"))
         {
            QueryManager.executeUpdate(connection, sql);
         }
      }
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private QInstance defineQInstance() throws QException
   {
      /////////////////////////////////////
      // basic definition of an instance //
      /////////////////////////////////////
      QInstance qInstance = new QInstance();
      qInstance.setAuthentication(new QAuthenticationMetaData().withType(QAuthenticationType.FULLY_ANONYMOUS));
      qInstance.addBackend(new RDBMSBackendMetaData()
         .withName(RDBMS_BACKEND_NAME)
         .withVendor("h2")
         .withHostName("mem")
         .withDatabaseName("test_database")
         .withUsername("sa"));

      ////////////////////////
      // configure the qbit //
      ////////////////////////
      UserRolePermissionsQBitConfig config = new UserRolePermissionsQBitConfig()
         .withUserTableConfig(ProvidedOrSuppliedTableConfig.provideTableUsingBackendNamed(RDBMS_BACKEND_NAME))
         .withTableMetaDataCustomizer((i, table) ->
         {
            if(table.getBackendName() == null)
            {
               table.setBackendName(RDBMS_BACKEND_NAME);
            }

            table.setBackendDetails(new RDBMSTableBackendDetails()
               .withTableName(QInstanceEnricher.inferBackendName(table.getName())));
            QInstanceEnricher.setInferredFieldBackendNames(table);

            return (table);
         });

      //////////////////////
      // produce our qbit //
      //////////////////////
      new UserRolePermissionsQBitProducer()
         .withUserRolePermissionsQBitConfig(config)
         .produce(qInstance);

      ///////////////////////////////////////////
      // turn off audits (why on by default??) //
      ///////////////////////////////////////////
      qInstance.getTables().values().forEach(t -> t.setAuditRules(new QAuditRules().withAuditLevel(AuditLevel.NONE)));
      return qInstance;
   }

}
