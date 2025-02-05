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

package io.oigres.ecomm.service.orders;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@SetEnvironmentVariable(key = "AWS_ENDPOINT", value = "http://localhost:4566")
@SetEnvironmentVariable(key = "AWS_REGION", value = "us-east-1")
@SetEnvironmentVariable(key = "AWS_DEFAULT_REGION", value = "us-east-1")
@SetEnvironmentVariable(key = "AWS_ACCESS_KEY_ID", value = "test")
@SetEnvironmentVariable(key = "AWS_SECRET_ACCESS_KEY", value = "test")
public class BootstrapTest {

  @Test
  void contextLoads() {}
}
