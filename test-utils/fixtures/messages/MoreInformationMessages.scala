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

object MoreInformationMessages {

  sealed trait ViewMessages { _: i18n =>
    val title: String
    val heading: String
    val hint: String
    val errorLength: String
    val errorRequired: String
    val errorXss: String
    val errorCharacter: String
  }

  object English extends ViewMessages with BaseEnglish {
    override val heading = "Give more information about why you are cancelling this movement"
    override val title = titleHelper(heading)
    override val hint = "Give information (optional)."
    override val errorLength = "Information must be 350 characters or less"
    override val errorRequired = "Enter information"
    override val errorXss = "Information must not include < and > and : and ;"
    override val errorCharacter = "Information must contain letters or numbers"
  }

  object Welsh extends ViewMessages with BaseWelsh {
    override val heading = "Give more information about why you are cancelling this movement"
    override val title = titleHelper(heading)
    override val hint = "Give information (optional)."
    override val errorLength = "Information must be 350 characters or less"
    override val errorRequired = "Enter information"
    override val errorXss = "Information must not include < and > and : and ;"
    override val errorCharacter = "Information must contain letters or numbers"
  }
}