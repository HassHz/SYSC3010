class flamestub {
	private int sensorValue;

	public flamestub(int sensorValue) {
		this.sensorValue = sensorValue;
	}

	public boolean isSensorActive() {
		if(sensorValue == 1) {
			return true;
		}
		return false;
	}
}
