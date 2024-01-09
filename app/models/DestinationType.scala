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

package models

sealed trait DestinationType {
  val auditValue: String
}

object DestinationType extends Enumerable.Implicits {

  case object TaxWarehouse extends WithName("1") with DestinationType {
    override val auditValue: String = "Tax warehouse"
  }
  case object RegisteredConsignee extends WithName("2") with DestinationType {
    override val auditValue: String = "Registered consignee"
  }
  case object TemporaryRegisteredConsignee extends WithName("3") with DestinationType {
    override val auditValue: String = "Temporary registered consignee"
  }
  case object DirectDelivery extends WithName("4") with DestinationType {
    override val auditValue: String = "Direct delivery"
  }
  case object ExemptedOrganisations extends WithName("5") with DestinationType {
    override val auditValue: String = "Exempted consignee"
  }
  case object Export extends WithName("6") with DestinationType {
    override val auditValue: String = "Export"
  }
  case object UnknownDestination extends WithName("8") with DestinationType {
    override val auditValue: String = "Unknown destination (consignee unknown)"
  }
  case object CertifiedConsignee extends WithName("9") with DestinationType {
    override val auditValue: String = "Certified Consignee"
  }
  case object TemporaryCertifiedConsignee extends WithName("10") with DestinationType {
    override val auditValue: String = "Temporary Certified Consignee"
  }
  case object ReturnToThePlaceOfDispatchOfTheConsignor extends WithName("11") with DestinationType {
    override val auditValue: String = "Return To The Place Of Dispatch Of The Consignor"
  }

  val values: Seq[DestinationType] = Seq(
    TaxWarehouse,
    RegisteredConsignee,
    TemporaryRegisteredConsignee,
    DirectDelivery,
    ExemptedOrganisations,
    Export,
    UnknownDestination,
    CertifiedConsignee,
    TemporaryCertifiedConsignee,
    ReturnToThePlaceOfDispatchOfTheConsignor
  )

  implicit val enumerable: Enumerable[DestinationType] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
