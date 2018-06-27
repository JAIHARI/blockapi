package it.unica.blockchain.analyses.litecoin.sql

import scalikejdbc._
import it.unica.blockchain.blockchains.BlockchainLib
import it.unica.blockchain.blockchains.litecoin.{LitecoinSettings, MainNet}
import it.unica.blockchain.db.sql.Table
import it.unica.blockchain.db.{DatabaseSettings, MySQL}
import it.unica.blockchain.utils.converter.DateConverter.convertDate


object LitecoinPools{
  def main(args: Array[String]): Unit ={

    val blockchain = BlockchainLib.getLitecoinBlockchain(new LitecoinSettings("user", "password", "9332", MainNet))
    val mySQL = new DatabaseSettings("myblockchainlite", MySQL, "root", "password")

    val startTime = System.currentTimeMillis()/1000

    val txTable = new Table(sql"""
      create table if not exists ltcpools(
        blockHash varchar(256) not null,
        timestamp TIMESTAMP not null,
        pool varchar(256) not null
      ) """,
      sql"""insert into ltcpools(blockHash, timestamp, pool) values (?, ?, ?)""",
      mySQL)


    blockchain.start(500000).foreach(block => {
        txTable.insert(Seq(block.hash.toString(), convertDate(block.date), block.getMiningPool()))
        if(block.height % 10000 == 0)
          println("Done working on block @ height " + block.height)
    })

    txTable.close

    val totalTime = System.currentTimeMillis() / 1000 - startTime

    println("Total time: " + totalTime)
    println("Computational time: " + (totalTime - Table.getWriteTime))
    println("Database time: " + Table.getWriteTime)
  }
}