package labManagerProtocal;

public class JoinRefusedException extends Throwable {
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Request to join has been refused";
	}
	
}
