import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*

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

inline fun Connection.executeUpdate(sql: String, action: (PreparedStatement) -> Unit) {
    var statement: PreparedStatement? = null
    try {
        statement = this.prepareStatement(sql)
        action(statement)
    } finally {
        statement?.close()
    }
}

fun <T> joinTruncateAfter(strings: List<T>, maxElements: Int, separator: String, mapper: (T) -> String): String {
    var result = strings.slice(0 until Math.min(maxElements, strings.size))
            .map(mapper)
            .joinToString(separator)
    if (strings.size > maxElements) {
       result += "$separator +${strings.size - maxElements} more"
    }
    return result
}
