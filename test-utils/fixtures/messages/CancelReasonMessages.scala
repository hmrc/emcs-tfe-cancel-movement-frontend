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

object CancelReasonMessages {

  sealed trait ViewMessages { _: i18n =>
    val title: String
    val heading: String
    val radio1: String
    val radio2: String
    val radio3: String
    val radio4: String
    val radio5: String
    val errorRequired: String
    val cyaLabel: String
    val cyaChangeHidden: String
  }

  object English extends ViewMessages with BaseEnglish {
    override val heading = "Why are you cancelling this movement?"
    override val title = titleHelper(heading)
    override val radio1 = "Commercial transaction interrupted"
    override val radio2 = "Duplicate electronic administrative document (eAD)"
    override val radio3 = "Goods were not dispatched on the date given in the eAD"
    override val radio4 = "The eAD contains an error"
    override val radio5 = "Other"
    override val errorRequired = "Select why you are cancelling this movement"
    override val cyaLabel = "Reason for cancelling"
    override val cyaChangeHidden = "reason for cancel"
  }
}