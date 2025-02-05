/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.orders.textsearch;

import io.oigres.ecomm.service.orders.domain.Order;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.ProgrammaticMappingConfigurationContext;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.TypeMappingStep;
import org.springframework.stereotype.Component;

@Component("searchMappingConfigurer")
public class SearchMappingConfigurer implements HibernateOrmSearchMappingConfigurer {

  @Override
  public void configure(HibernateOrmMappingConfigurationContext context) {
    ProgrammaticMappingConfigurationContext mapping = context.programmaticMapping();
    configureOrderIndexes(mapping);
  }

  private void configureOrderIndexes(ProgrammaticMappingConfigurationContext mapping) {
    TypeMappingStep orderMapping = mapping.type(Order.class);
    orderMapping.indexed();
    orderMapping.property("id").documentId();
  }
}
