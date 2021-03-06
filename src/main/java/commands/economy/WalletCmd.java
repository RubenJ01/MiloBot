package commands.economy;

import commands.Command;
import database.DatabaseManager;
import database.queries.UserTableQueries;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WalletCmd extends Command implements MorbconomyCmd {

	private final DatabaseManager dbManager;

	public WalletCmd() {
		this.commandName = "wallet";
		this.commandDescription = "Check your wallet.";
		this.dbManager = DatabaseManager.getInstance();
	}

	@Override
	public void executeCommand(@NotNull MessageReceivedEvent event, @NotNull List<String> args) {
		String authorId = event.getAuthor().getId();

		ArrayList<String> query = dbManager.query(UserTableQueries.getUserCurrency, DatabaseManager.QueryTypes.RETURN, authorId);
		int wallet = Integer.parseInt(query.get(0));

		event.getChannel().sendMessage(String.format("You have `%d` morbcoins in your wallet.", wallet)).queue();
	}
}
