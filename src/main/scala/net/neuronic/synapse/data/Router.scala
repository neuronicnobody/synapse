package net.neuronic.synapse.data

import net.neuronic.synapse.observe._
import net.neuronic.synapse.visualize._
import net.neuronic.synapse.detect._

import scala.collection.mutable.{HashMap, ListBuffer}

object Router extends Observer {

  private val dcmap = HashMap[Symbol, DetectorCollection]()
  private val chart = new ChartFrame

  // route observation to collection of detectors based on instrument 
  def route(o:Observation) = {

    (dcmap get o.instr) match {
      case Some(dc) => dc notify o
      case None => {
        val dc = new DetectorCollection(this)
        dc notify o
        dcmap += (o.instr -> dc) 
      }
    }

  }

  override def notify(o:Observation) = o match {
    case p:PricePoint => {
      println(p)
      chart plot p
    }
    case s:PriceSegment => {
      //println(s)
    }
    case w:PriceWave => {
      println(w)
      chart mark w
    }
    case _ => {
    }
  }
}
