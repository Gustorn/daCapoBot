import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.Properties

fun createProperties(keyValuePairs: Iterable<Map.Entry<String, String>>): Properties {
    val props = Properties()
    for ((key, value) in keyValuePairs) {
        props.setProperty(key, value)
    }
    return props
}

inline fun Connection.processQuery(sql: String, action: (ResultSet) -> Unit) {
    var statement: PreparedStatement? = null
    try {
        statement = this.prepareStatement(sql)
        val results = statement.executeQuery()
        while (results.next()) {
            action(results)
        }
    } finally {
        statement?.close()
    }
}

inline fun <T> Connection.processSingle(sql: String, action: (ResultSet) -> T): T {
    var statement: PreparedStatement? = null
    try {
        statement = this.prepareStatement(sql)
        val results = statement.executeQuery()
        return action(results)
    } finally {
        statement?.close()
    }
}

inline fun Connection.executeUpdate(sql: String, action: (PreparedStatement) -> Unit) {
    var statement: PreparedStatement? = null
    try {
        statement = this.prepareStatement(sql)
        action(statement)
    } finally {
        statement?.close()
    }
}
