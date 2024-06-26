/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fixtures.messages

import fixtures.i18n

object UnauthorisedMessages {

  sealed trait ViewMessages { _: i18n =>
    val title: String
    val heading: String
    val p1: String
    val p2: String
    val bullet1: String
    val bullet2: String
    val p3: String
  }

  object English extends ViewMessages with BaseEnglish {
    override val heading = "There is a problem"
    override val title = titleHelper("There is a problem")
    override val p1 = "You do not have permission to view this page."
    override val p2 = "You should:"
    override val bullet1 = "check you have signed in with the right details"
    override val bullet2 = "check you are using the right excise reference number (ERN)"
    override val p3 = "If you are using the right sign in details and ERN, check the web address is correct."
  }

}
