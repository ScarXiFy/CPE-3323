package com.usc.cems.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class PlaceholderDao_Impl(
  __db: RoomDatabase,
) : PlaceholderDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPlaceholderEntity: EntityInsertAdapter<PlaceholderEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPlaceholderEntity = object : EntityInsertAdapter<PlaceholderEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `placeholder_table` (`id`,`title`) VALUES (nullif(?, 0),?)"

      protected override fun bind(statement: SQLiteStatement, entity: PlaceholderEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.title)
      }
    }
  }

  public override suspend fun insert(entity: PlaceholderEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfPlaceholderEntity.insertAndReturnId(_connection, entity)
    _result
  }

  public override suspend fun getAll(): List<PlaceholderEntity> {
    val _sql: String = "SELECT * FROM placeholder_table"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _result: MutableList<PlaceholderEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PlaceholderEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          _item = PlaceholderEntity(_tmpId,_tmpTitle)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
