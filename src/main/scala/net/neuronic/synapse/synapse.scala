package net.neuronic.synapse

import net.neuronic.synapse.data._
import net.neuronic.synapse.visualize._
import net.neuronic.synapse.detect._

import com.mongodb.casbah.Imports._
import java.util.Date

object Synapse {
  def main(args:Array[String]) = {

    // run some test data, see what's shakin'
    MongoConnection()("ticks")("ticks").find.foreach { t =>
      val bid = t.get("bid").asInstanceOf[Double]
      val ask = t.get("ask").asInstanceOf[Double]
      val rec = t.get("recLoc").asInstanceOf[Date]

      Thread.sleep(500)
      Router.route(PricePoint(bid, 'Bid, 'EURUSD, rec))
      Router.route(PricePoint(ask, 'Ask, 'EURUSD, rec))
    }

  }
}
