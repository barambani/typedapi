package typedapi

import typedapi.shared._
import shapeless.{Witness, HNil}

package object dsl {

  def := = EmptyCons

  def Path[S](wit: Witness.Lt[S]) = PathElement[S](wit)
  def Segment[A]                  = new SegmentHelper[A]
  def Query[A]                    = new QueryHelper[A]
  def Header[A]                   = new HeaderHelper[A]
  val FixedHeaders                = new FixedHeadersHelper[HNil]()
  val RawHeaders                  = RawHeadersParam

  type Json  = `Application/Json`.type
  type Plain = `Text/Plain`.type

  def ReqBody[MT <: MediaType, A] = ReqBodyElement[MT, A]
  def Get[MT <: MediaType, A] = GetElement[MT, A]
  def Put[MT <: MediaType, A] = PutElement[MT, A]
  def Post[MT <: MediaType, A] = PostElement[MT, A]
  def Delete[MT <: MediaType, A] = DeleteElement[MT, A]
}
