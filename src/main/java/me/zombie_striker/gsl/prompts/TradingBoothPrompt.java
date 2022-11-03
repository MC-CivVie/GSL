package me.zombie_striker.gsl.prompts;

import java.util.regex.Pattern;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TradingBoothPrompt extends ValidatingPrompt implements @Nullable Prompt {
  @Override
  public String getPromptText(ConversationContext ctx) {
    // NOTE: null is special in the map, do not overwrite it!
    // you should put your first message into the null key, as it will be sent first.
    return "Data: " + ctx.getSessionData("null");
  }

  @Override
  public Prompt acceptValidatedInput(ConversationContext ctx, String in) {
    if(in.equalsIgnoreCase("cancel")) {
      return END_OF_CONVERSATION;
    } else {
      ctx.setSessionData("data", in);
    } return END_OF_CONVERSATION;
  }

  protected boolean isInputValid(ConversationContext ctx, String in) {
    // NOTE: mat54 matches, "mat 54" does not.
    // this regex will match matxx, mat, or xx (xx being the amount of items, up to 64).
    return Pattern.matches("^(mat){0,1}(\\d{1,2}|\\b)$", in);
  }

  @Override
  public boolean blocksForInput(@NotNull ConversationContext context) {
	  return true;
  }
}
