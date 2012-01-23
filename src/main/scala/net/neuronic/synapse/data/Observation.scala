package net.neuronic.synapse.data

import scala.collection.mutable.ListBuffer
import java.util.{Calendar, Date, Timer}

trait Observation {
  def series: Symbol
  def instr: Symbol
}

case class PricePoint(price:Double, series:Symbol, instr:Symbol, rec:Date) extends Observation 

case class PriceSegment(start:PricePoint, end:PricePoint) extends Observation {

  val instr: Symbol = start.instr
  val series: Symbol = start.series

  def this(points:List[PricePoint]) = this(points.head, points.last)

  def isTrough: Boolean = end.price > start.price
  
  def isPeak: Boolean = end.price < start.price

  def isFlat: Boolean = end.price == start.price

  def opposes(that:PriceSegment): Boolean = {
    (this.isTrough && that.isPeak) || (this.isPeak && that.isTrough) 
  }

  // stricter version of oppose predicate, includes flat segments
  def confronts(that:PriceSegment): Boolean = (this opposes that) || this.isFlat

  def getPricePoints(): ListBuffer[PricePoint] = {
    ListBuffer[PricePoint](start, end)
  }
  
}

case class PriceBar(open:Double, high:Double, low:Double, close:Double, instr:Symbol, series:Symbol, rec:Date, start:Date, end:Date) extends Observation 

case class PriceWave private (start:PricePoint, end:PricePoint) extends Observation {
  val instr: Symbol = start.instr
  val series: Symbol = start.series

  // secondary constructor
  def this(seg:PriceSegment) = this(seg.start, seg.end)

  // water is rising
  def isTrough: Boolean = end.price > start.price
  
  // its falling
  def isPeak: Boolean = end.price < start.price

  def opposes(that:PriceWave): Boolean = {
    (this.isTrough && that.isPeak) || (this.isPeak && that.isTrough) 
  }

  def getPricePoints(): ListBuffer[PricePoint] = {
    ListBuffer[PricePoint](start, end) 
  } 
}

object PriceWave {
  def apply(segs:List[PriceSegment]): PriceWave = {
    // map price segs to waves and then reduce into one super wave
    (segs map { new PriceWave(_) }) reduce { (a, b) => new PriceWave(a.start, b.end) }
  }
}

case class Divergence(top:PriceWave, bottom:PriceWave) {
  def getPricePoints(): ListBuffer[PricePoint] = {
    top.getPricePoints ++ bottom.getPricePoints
  }
}



