package net.neuronic.synapse.detect

import net.neuronic.synapse.observe._
import net.neuronic.synapse.data._

import scala.collection.mutable.{ListBuffer, HashMap}

class DetectorCollection(obs:Observer) extends SubjectObserver {

  // detect segments from points
  subscribe( new Detector[PricePoint, PriceSegment](this, 
    (o:PricePoint, buf:ListBuffer[PricePoint]) => {
     buf += o
     if(buf.size > 1) {
       val s = Some(new PriceSegment(buf.toList))
       buf remove 0
       s
     } else {
      None
     }
   }) )

  // detect waves from segments
  subscribe( new Detector[PriceSegment, PriceWave](this, 
    (o:PriceSegment, buf:ListBuffer[PriceSegment]) => {
     if( buf exists { _ opposes o } ) {
       val w = Some(PriceWave(buf.toList))
       buf.clear
       buf += o
       w
     } else {
       buf += o
       None
     }
   }) )

  subscribe(obs)

  override def notify(o:Observation) = publish(o) 

}
