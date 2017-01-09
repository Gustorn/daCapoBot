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

inline fun <T> Iterable<T>.joinToString(truncateAfter: Int, separator: String, mapper: (T) -> String): String {
    val iterator = this.iterator()
    if (!iterator.hasNext()) {
        return ""
    }

    val builder  = StringBuilder()
    builder.append(mapper(iterator.next()))

    var elements = 1
    while (iterator.hasNext() && elements < truncateAfter) {
        builder.append(separator)
        builder.append(mapper(iterator.next()))
        elements += 1
    }

    if (elements > truncateAfter) {
        builder.append(separator)
        builder.append("+")
        builder.append(elements - truncateAfter)
        builder.append(" more")
    }

    return builder.toString()
}
