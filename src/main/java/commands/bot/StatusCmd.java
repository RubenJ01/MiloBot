package commands.bot;

import commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import utility.EmbedUtils;

import java.util.List;

/**
 * The status command.
 * Shows some information on the bots current status.
 */
public class StatusCmd extends Command implements BotCmd {

	public StatusCmd() {
		this.commandName = "status";
		this.commandDescription = "The status of the bot.";
		this.cooldown = 60;
	}

	@Override
	public void executeCommand(@NotNull MessageReceivedEvent event, List<String> args) {
		EmbedBuilder embed = new EmbedBuilder();
		EmbedUtils.styleEmbed(embed, event.getAuthor());

		JDA jda = event.getJDA();
		List<Guild> guilds = jda.getGuilds();
		int amountOfGuilds = guilds.size();
		int memberTotal = guilds.stream().mapToInt(Guild::getMemberCount).sum();
		int emoteTotal = jda.getEmotes().size();
		long gatewayPing = jda.getGatewayPing();
		int categories = jda.getCategories().size();
		int textChannels = jda.getTextChannels().size();
		int voiceChannels = jda.getVoiceChannels().size();
		int roleTotal = jda.getRoles().size();

		embed.addField("Ping", String.format("The response time was %d milliseconds.", gatewayPing), false);
		embed.addField("Servers", String.format("The bot is running in %d servers for a total of %d members.",
				amountOfGuilds, memberTotal), false);
		embed.addField("Channels", String.format("The bot is looking for commands in %d text channels, " +
						"in %d different categories and can see %d voice channels.", textChannels,
				categories, voiceChannels), false);
		embed.addField("Random Information", String.format("The bot can see %d emotes and %d roles.",
				emoteTotal, roleTotal), false);

		event.getChannel().sendTyping().queue();
		event.getChannel().sendMessageEmbeds(embed.build()).setActionRow(
				Button.secondary(event.getAuthor().getId() + ":delete", "Delete")).queue();
	}

}
