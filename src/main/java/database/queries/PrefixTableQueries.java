package database.queries;

public interface PrefixTableQueries {
	String createPrefixTable = "CREATE TABLE IF NOT EXISTS prefix (serverId varchar(255), prefix varchar(255));";
	String addServerPrefix = "INSERT INTO prefix(serverId, prefix) VALUES(?, ?);";
	String deleteServerPrefix = "DELETE FROM prefix WHERE serverId = ?;";
	String updateServerPrefix = "UPDATE prefix SET prefix = ? WHERE serverId = ?;";
	String getAllPrefixes = "SELECT serverId, prefix FROM prefix;";
}
