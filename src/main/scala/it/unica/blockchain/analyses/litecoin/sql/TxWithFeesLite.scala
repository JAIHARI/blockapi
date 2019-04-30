package it.unica.blockchain.analyses.litecoin.sql
/*
import scalikejdbc._
import it.unica.blockchain.blockchains.BlockchainLib
import it.unica.blockchain.blockchains.litecoin.{LitecoinSettings, MainNet}
import it.unica.blockchain.externaldata.rates.LitecoinRates
import it.unica.blockchain.db.sql.Table
import it.unica.blockchain.db.{DatabaseSettings, MySQL}
import it.unica.blockchain.utils.converter.DateConverter
import it.unica.blockchain.utils.converter.DateConverter.convertDate

/**
  * Created by Giulia on 15/05/2018.
  */
object TxWithFeesLite {
  def main(args: Array[String]): Unit = {

    val blockchain = BlockchainLib.getLitecoinBlockchain(new LitecoinSettings("user", "password", "9332", MainNet, true))
    val mySQL = new DatabaseSettings("fees", MySQL, "user", "password")


    val startTime = System.currentTimeMillis() / 1000

    val txTable = new Table(
      sql"""
      create table if not exists txfeeslite(
        id serial not null primary key,
        blockHash varchar(256) not null,
        transactionHash varchar(256) not null,
        txdate TIMESTAMP not null,
        fee bigint,
        rate float
    )""",
      sql"""insert into txfeeslite (blockHash, transactionHash, txdate, fee, rate) values(?,?,?,?,?)""",
      mySQL)


    blockchain.start(500000).end(1200000).foreach(block => {

      if (block.height % 10 == 0) println(DateConverter.formatTimestamp(System.currentTimeMillis()) + " - Block: " + block.height)

      block.txs.foreach(tx => {
        txTable.insert(Seq(
          block.hash.toString,
          tx.hash.toString,
          convertDate(block.date),
          (tx.getInputsSum() - tx.getOutputsSum()),
          LitecoinRates.getRate(block.date)))
      })
    })

    txTable.close

    val totalTime = System.currentTimeMillis() / 1000 - startTime

    println("Total time: " + totalTime)
    println("Computational time: " + (totalTime - Table.getWriteTime))
    println("Database time: " + Table.getWriteTime)
  }
}
*/