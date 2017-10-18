package Search.commands.override;

import java.util.HashMap;

import Search.global.record.Log;
import Search.global.record.Settings;
import Search.googleutil.drive.DataEnum;
import Search.googleutil.drive.DriveFile;
import Search.googleutil.drive.DriveManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.Lib;

public class DrivePush extends OverrideGenerics implements OverrideCommand{

	@Override
	public void action(HashMap<String, String[]> args, MessageReceivedEvent event) {
		DriveManager.update(new DriveFile(Settings.dataSource,DataEnum.SearchData.id));
		DriveManager.update(new DriveFile(Log.LogSource,DataEnum.LogSource.id));
		Lib.sendMessage(event, "Pushed files to Google Drive");
	}

	@Override
	public void help(MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
