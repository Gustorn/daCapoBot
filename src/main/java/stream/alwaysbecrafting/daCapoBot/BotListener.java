package stream.alwaysbecrafting.daCapoBot;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.MessageEvent;

//==============================================================================
class BotListener extends ListenerAdapter{
	//--------------------------------------------------------------------------
	@Override
	public void onMessage(MessageEvent event) throws Exception{
		if(event.getUser().getRealName() != null) {
			Database.DB_INSTANCE.logChat( event.getUser().getRealName(), event.getMessage() );
		}
		if (event.getMessage().toLowerCase().startsWith( "!help" ))
			event.respondWith( "!whoami, !help, !veto, !request, !suggestions"  );
		if (event.getMessage().toLowerCase().startsWith( "!veto" ))
			Database.DB_INSTANCE.veto();
		if (event.getMessage().toLowerCase().startsWith( "!request" ))
			Database.DB_INSTANCE.request(event.getMessage().toLowerCase());
		if (event.getMessage().toLowerCase().startsWith( "!whoami" ))
			event.respondWith( "I'm your friendly music bot. For a list of commands type !help. " +
					"for my source code visit https://github.com/AlwaysBeCrafting/daCapoBot" );
		if (event.getMessage().toLowerCase().startsWith( "!test" ))
			event.respondWith( "Slippy's not such a screw up after all" );
		if (event.getMessage().toLowerCase().startsWith( "!suggestions" ))
			event.respondWith( "Feature requests are welcome, please submit to https://github.com/AlwaysBeCrafting/daCapoBot/issues" );

	}

	@Override
	public void onConnect( ConnectEvent event ){
		try {
			Database.DB_INSTANCE.addMP3s(new Config().getMusicDir());

			Player p1 = new Player();
			p1.play();
		}
		catch(Exception e){e.printStackTrace();}
	}

	//--------------------------------------------------------------------------
}
//------------------------------------------------------------------------------
