package tradetemplate

import cats._, implicits._

sealed abstract class Currency
case object EUR extends Currency
case object USD extends Currency

final case class TradeTemplate(
  payments: List[java.time.LocalDate],
  ccy: Option[Currency],
  otc: Option[Boolean]
)
object TradeTemplate {
  def lastWins[A]: Monoid[Option[A]] = Monoid.instance(
    None,
    {
      case (None, None) => None
      case (only, None) => only
      case (None, only) => only
      case (_, winner)  => winner
    }
  )

  implicit val monoidCcy: Monoid[Option[Currency]] = lastWins
  implicit val monoidOtc: Monoid[Option[Boolean]] = lastWins

  implicit val monoid: Monoid[TradeTemplate] = Monoid.instance(
    TradeTemplate(Nil, None, None),
    (a, b) => TradeTemplate(a.payments |+| b.payments, b.ccy |+| a.ccy, b.otc |+| a.otc)
  )

  // implicit val monoid: Monoid[TradeTemplate] = Monoid.instance(
  //   TradeTemplate(Nil, None, None),
  //   (a, b) => TradeTemplate(a.payments |+| b.payments, b.ccy <+> a.ccy, b.otc <+> a.otc)
  // )

  // implicit val monoid: Monoid[TradeTemplate] = Monoid.instance(
  //   (a, b) =>
  //     TradeTemplate(
  //       a.payments |+| b.payments,
  //       a.ccy |+| b.ccy,
  //       a.otc |+| b.otc
  //     ),
  //   TradeTemplate(Nil, Tag(None), Tag(None))
  // )
}
