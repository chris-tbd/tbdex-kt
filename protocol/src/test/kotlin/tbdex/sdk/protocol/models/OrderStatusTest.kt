package tbdex.sdk.protocol.models

import assertk.assertThat
import assertk.assertions.isEqualTo
import de.fxlae.typeid.TypeId
import org.junit.jupiter.api.assertDoesNotThrow
import tbdex.sdk.protocol.TestData
import tbdex.sdk.protocol.serialization.Json
import kotlin.test.Test
import kotlin.test.assertIs

class OrderStatusTest {
  @Test
  fun `can create a new orderStatus`() {
    val orderStatus = OrderStatus.create(
      to = "pfi",
      from = "alice",
      exchangeId = TypeId.generate(MessageKind.rfq.name),
      orderStatusData = OrderStatusData("my status")
    )

    assertk.assertAll {
      assertThat(orderStatus.metadata.id.prefix).isEqualTo("orderstatus")
      assertThat(orderStatus.data.orderStatus).isEqualTo("my status")
    }
  }

  @Test
  fun `can parse an orderStatus from a json string`() {
    val orderStatus = TestData.getOrderStatus()
    orderStatus.sign(TestData.PFI_DID)
    val jsonMessage = orderStatus.toString()
    val parsedMessage = Message.parse(jsonMessage)

    assertIs<OrderStatus>(parsedMessage)
    assertThat(parsedMessage.toString()).isEqualTo(jsonMessage)
  }

  @Test
  fun `can validate an orderStatus`() {
    val orderStatus = TestData.getOrderStatus()
    orderStatus.sign(TestData.PFI_DID)

    assertDoesNotThrow { Message.parse(Json.stringify(orderStatus)) }
  }
}
