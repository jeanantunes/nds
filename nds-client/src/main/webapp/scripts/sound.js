function playSoundError() {
	
	var audioFile = contextPath+"/sound/error.mp3";
	
	var sourceAudio = document.createElement("source");
		sourceAudio.setAttribute("src", audioFile);
	
	var tagAudio = document.createElement("audio");
		tagAudio.className = "sound-error";
		tagAudio.appendChild(sourceAudio);
		
	tagAudio.play();
	
}