package polytech.devint.view;

import polytech.devint.view.sound.Sound;

import java.util.Optional;

/**
 * Represent an "help" that could be use in a game for example, or when switching menu to help the
 * user to know what he can do
 *
 * @author Loris Friedel
 */
public class ContextHelp {

  private Optional<String> text;
  private Optional<Sound> audio;

  private Optional<String> detailedText;
  private Optional<Sound> detailedAudio;

  /**
   * Instantiate a default context help, empty.
   */
  public ContextHelp() {
    this(null, null, null, null);
  }

  /**
   * Instantiate a context help with its associated audio and the details help text with its
   * associated detailed audio
   *
   * @param text              help text of the context
   * @param audioHelp         the audio of the help (a voice recording for example)
   * @param detailedText      detailed help text of the context
   * @param detailedAudioHelp the audio of the detailed help (a voice recording for example)
   */
  private ContextHelp(String text, Sound audioHelp, String detailedText, Sound detailedAudioHelp) {
    this.text = Optional.ofNullable(text);
    this.audio = Optional.ofNullable(audioHelp);
    this.detailedText = Optional.ofNullable(detailedText);
    this.detailedAudio = Optional.ofNullable(detailedAudioHelp);
  }

  /**
   * Instantiate a context help with its associated audio and only the details help text (not its
   * associated audio)
   *
   * @param text         help text of the context
   * @param audio        the audio of the help (a voice recording for example)
   * @param detailedText detailed help text of the context
   */
  public ContextHelp(String text, Sound audio, String detailedText) {
    this(text, audio, detailedText, null);
  }


  /**
   * Instantiate a context help without an associated audio and only the details help text without
   * an associated audio too All text will be played by the voice synthesis if needed
   *
   * @param text         help text of the context
   * @param detailedText detailed help text of the context
   */
  public ContextHelp(String text, String detailedText) {
    this(text, null, detailedText, null);
  }

  /**
   * Instantiate a context help with its associated audio but not details help text and audio
   *
   * @param text  help text of the context
   * @param audio the audio of the help (a voice recording for example)
   */
  public ContextHelp(String text, Sound audio) {
    this(text, audio, null, null);
  }

  /**
   * Instantiate a context help that do not contains an associated audio nor detailed help text and
   * sound No associated audio means the text will be played be the voice synthesis when needed
   *
   * @param text help text of the context
   */
  public ContextHelp(String text) {
    this(text, null, null, null);
  }

  /**
   * @return the help text of this context
   */
  public Optional<String> getText() {
    return text;
  }

  /**
   * @return the detailed help of this context
   */
  public Optional<String> getDetailedText() {
    return detailedText;
  }

  /**
   * Play the help on speaker
   */
  public void play() { // TODO To remove
    if (audio.isPresent()) {
      audio.get().play();
    } else {
      if (text.isPresent()) {
        // TODO : synthesis.playText(text)
      }
    }
  }

  /**
   * Play the detailed help on speaker
   */
  public void playDetailed() { // TODO To remove
    if (detailedAudio.isPresent()) {
      detailedAudio.get().play();
    } else {
      if (detailedText.isPresent()) {
        // TODO : synthesis.playText(detailedText)
      }
    }
  }

  public void stop() { // TODO To remove
    if (audio.isPresent()) {
      audio.get().play();
    }
  }

  public void stopDetailed() { // TODO To remove
    if (detailedAudio.isPresent()) {
      detailedAudio.get().play();
    }
  }

  public Optional<Sound> getAudio() {
    return audio;
  }

  public Optional<Sound> getDetailedAudio() {
    return detailedAudio;
  }

  public void updateText(String text) {
    this.text = Optional.ofNullable(text);
  }

  public void updateAudio(Sound audio) {
    this.audio = Optional.ofNullable(audio);
  }

  public void updateDetailedText(String detailedText) {
    this.detailedText = Optional.ofNullable(detailedText);
  }

  public void updateDetailedAudio(Sound detailedAudio) {
    this.detailedAudio = Optional.ofNullable(detailedAudio);
  }
}
