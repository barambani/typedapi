package typedapi

import typedapi.shared._
import shapeless._
import shapeless.labelled.FieldType
import shapeless.ops.hlist.Tupler
import shapeless.ops.function.FnFromProduct

package object client extends TypeLevelFoldLeftLowPrio 
                      with TypeLevelFoldLeftListLowPrio
                      with WitnessToStringLowPrio
                      with ApiTransformer {

  def deriveUriString(cm: ClientManager[_], uri: List[String]): String = cm.base + "/" + uri.mkString("/")

  def derive[H <: HList, El <: HList, KIn <: HList, VIn <: HList, M <: MethodType, MT <: MediaType, Out, D <: HList]
    (apiList: ApiTypeCarrier[H])
    (implicit folder: Lazy[TypeLevelFoldLeft.Aux[H, Unit, (El, KIn, VIn, M, FieldType[MT, Out])]],
              builder: RequestDataBuilder.Aux[El, KIn, VIn, M, FieldType[MT, Out], D],
              inToFn: FnFromProduct[VIn => ExecutableDerivation[El, KIn, VIn, M, MT, Out, D]]): inToFn.Out =
    inToFn.apply(input => new ExecutableDerivation[El, KIn, VIn, M, MT, Out, D](builder, input))

  def deriveAll[H <: HList, In <: HList, Fold <: HList, B <: HList, Ex <: HList](apiLists: CompositionCons[H])
                                                                                (implicit folders: TypeLevelFoldLeftList.Aux[H, Fold],
                                                                                          builderList: RequestDataBuilderList.Aux[Fold, B],
                                                                                          executables: ExecutablesFromHList.Aux[B, Ex],
                                                                                          tupler: Tupler[Ex]): tupler.Out =
    executables(builderList.builders).tupled
}
