package at.bestsolution.efxclipse.tooling.css.serializer;

import at.bestsolution.efxclipse.tooling.css.services.CssDslGrammarAccess;
import com.google.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.AbstractElementAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.AlternativeAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.GroupAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.TokenAlias;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynTransition;
import org.eclipse.xtext.serializer.sequencer.AbstractSyntacticSequencer;

@SuppressWarnings("restriction")
public class AbstractCssDslSyntacticSequencer extends AbstractSyntacticSequencer {

	protected CssDslGrammarAccess grammarAccess;
	protected AbstractElementAlias match_charset_CHARSETKeyword_0_1_or_CharsetKeyword_0_0;
	protected AbstractElementAlias match_function_HyphenMinusKeyword_0_q;
	protected AbstractElementAlias match_function_WSTerminalRuleCall_3_p;
	protected AbstractElementAlias match_function_WSTerminalRuleCall_5_a;
	protected AbstractElementAlias match_importExpression_IMPORTKeyword_0_0_1_or_ImportKeyword_0_0_0;
	protected AbstractElementAlias match_media_MEDIAKeyword_0_1_or_MediaKeyword_0_0;
	protected AbstractElementAlias match_page_PAGEKeyword_1_1_or_PageKeyword_1_0;
	protected AbstractElementAlias match_page_SemicolonKeyword_5_0_a;
	protected AbstractElementAlias match_page_SemicolonKeyword_5_0_p;
	protected AbstractElementAlias match_page___PAGEKeyword_1_1_or_PageKeyword_1_0___LeftCurlyBracketKeyword_3_SemicolonKeyword_5_0_a;
	protected AbstractElementAlias match_page___PAGEKeyword_1_1_or_PageKeyword_1_0___LeftCurlyBracketKeyword_3_SemicolonKeyword_5_0_p;
	protected AbstractElementAlias match_selector_WSTerminalRuleCall_1_1_0_p;
	
	@Inject
	protected void init(IGrammarAccess access) {
		grammarAccess = (CssDslGrammarAccess) access;
		match_charset_CHARSETKeyword_0_1_or_CharsetKeyword_0_0 = new AlternativeAlias(false, false, new TokenAlias(false, false, grammarAccess.getCharsetAccess().getCHARSETKeyword_0_1()), new TokenAlias(false, false, grammarAccess.getCharsetAccess().getCharsetKeyword_0_0()));
		match_function_HyphenMinusKeyword_0_q = new TokenAlias(true, false, grammarAccess.getFunctionAccess().getHyphenMinusKeyword_0());
		match_function_WSTerminalRuleCall_3_p = new TokenAlias(false, true, grammarAccess.getFunctionAccess().getWSTerminalRuleCall_3());
		match_function_WSTerminalRuleCall_5_a = new TokenAlias(true, true, grammarAccess.getFunctionAccess().getWSTerminalRuleCall_5());
		match_importExpression_IMPORTKeyword_0_0_1_or_ImportKeyword_0_0_0 = new AlternativeAlias(false, false, new TokenAlias(false, false, grammarAccess.getImportExpressionAccess().getIMPORTKeyword_0_0_1()), new TokenAlias(false, false, grammarAccess.getImportExpressionAccess().getImportKeyword_0_0_0()));
		match_media_MEDIAKeyword_0_1_or_MediaKeyword_0_0 = new AlternativeAlias(false, false, new TokenAlias(false, false, grammarAccess.getMediaAccess().getMEDIAKeyword_0_1()), new TokenAlias(false, false, grammarAccess.getMediaAccess().getMediaKeyword_0_0()));
		match_page_PAGEKeyword_1_1_or_PageKeyword_1_0 = new AlternativeAlias(false, false, new TokenAlias(false, false, grammarAccess.getPageAccess().getPAGEKeyword_1_1()), new TokenAlias(false, false, grammarAccess.getPageAccess().getPageKeyword_1_0()));
		match_page_SemicolonKeyword_5_0_a = new TokenAlias(true, true, grammarAccess.getPageAccess().getSemicolonKeyword_5_0());
		match_page_SemicolonKeyword_5_0_p = new TokenAlias(false, true, grammarAccess.getPageAccess().getSemicolonKeyword_5_0());
		match_page___PAGEKeyword_1_1_or_PageKeyword_1_0___LeftCurlyBracketKeyword_3_SemicolonKeyword_5_0_a = new GroupAlias(false, false, new AlternativeAlias(false, false, new TokenAlias(false, false, grammarAccess.getPageAccess().getPAGEKeyword_1_1()), new TokenAlias(false, false, grammarAccess.getPageAccess().getPageKeyword_1_0())), new TokenAlias(false, false, grammarAccess.getPageAccess().getLeftCurlyBracketKeyword_3()), new TokenAlias(true, true, grammarAccess.getPageAccess().getSemicolonKeyword_5_0()));
		match_page___PAGEKeyword_1_1_or_PageKeyword_1_0___LeftCurlyBracketKeyword_3_SemicolonKeyword_5_0_p = new GroupAlias(false, false, new AlternativeAlias(false, false, new TokenAlias(false, false, grammarAccess.getPageAccess().getPAGEKeyword_1_1()), new TokenAlias(false, false, grammarAccess.getPageAccess().getPageKeyword_1_0())), new TokenAlias(false, false, grammarAccess.getPageAccess().getLeftCurlyBracketKeyword_3()), new TokenAlias(false, true, grammarAccess.getPageAccess().getSemicolonKeyword_5_0()));
		match_selector_WSTerminalRuleCall_1_1_0_p = new TokenAlias(false, true, grammarAccess.getSelectorAccess().getWSTerminalRuleCall_1_1_0());
	}
	
	@Override
	protected String getUnassignedRuleCallToken(RuleCall ruleCall, INode node) {
		if(ruleCall.getRule() == grammarAccess.getCOMMARule())
			return getCOMMAToken(ruleCall, node);
		else if(ruleCall.getRule() == grammarAccess.getWSRule())
			return getWSToken(ruleCall, node);
		else if(ruleCall.getRule() == grammarAccess.getOperatorRule())
			return getoperatorToken(ruleCall, node);
		return "";
	}
	
	protected String getCOMMAToken(RuleCall ruleCall, INode node) {
		if (node != null)
			return getTokenText(node);
		return ",";
	}
	protected String getWSToken(RuleCall ruleCall, INode node) {
		if (node != null)
			return getTokenText(node);
		return " ";
	}
	protected String getoperatorToken(RuleCall ruleCall, INode node) {
		if (node != null)
			return getTokenText(node);
		return "/";
	}
	
	@Override
	protected void emitUnassignedTokens(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		if (!transition.isSyntacticallyAmbiguous())
			return;
		if(match_charset_CHARSETKeyword_0_1_or_CharsetKeyword_0_0.equals(transition.getAmbiguousSyntax()))
			emit_charset_CHARSETKeyword_0_1_or_CharsetKeyword_0_0(semanticObject, transition, fromNode, toNode);
		else if(match_function_HyphenMinusKeyword_0_q.equals(transition.getAmbiguousSyntax()))
			emit_function_HyphenMinusKeyword_0_q(semanticObject, transition, fromNode, toNode);
		else if(match_function_WSTerminalRuleCall_3_p.equals(transition.getAmbiguousSyntax()))
			emit_function_WSTerminalRuleCall_3_p(semanticObject, transition, fromNode, toNode);
		else if(match_function_WSTerminalRuleCall_5_a.equals(transition.getAmbiguousSyntax()))
			emit_function_WSTerminalRuleCall_5_a(semanticObject, transition, fromNode, toNode);
		else if(match_importExpression_IMPORTKeyword_0_0_1_or_ImportKeyword_0_0_0.equals(transition.getAmbiguousSyntax()))
			emit_importExpression_IMPORTKeyword_0_0_1_or_ImportKeyword_0_0_0(semanticObject, transition, fromNode, toNode);
		else if(match_media_MEDIAKeyword_0_1_or_MediaKeyword_0_0.equals(transition.getAmbiguousSyntax()))
			emit_media_MEDIAKeyword_0_1_or_MediaKeyword_0_0(semanticObject, transition, fromNode, toNode);
		else if(match_page_PAGEKeyword_1_1_or_PageKeyword_1_0.equals(transition.getAmbiguousSyntax()))
			emit_page_PAGEKeyword_1_1_or_PageKeyword_1_0(semanticObject, transition, fromNode, toNode);
		else if(match_page_SemicolonKeyword_5_0_a.equals(transition.getAmbiguousSyntax()))
			emit_page_SemicolonKeyword_5_0_a(semanticObject, transition, fromNode, toNode);
		else if(match_page_SemicolonKeyword_5_0_p.equals(transition.getAmbiguousSyntax()))
			emit_page_SemicolonKeyword_5_0_p(semanticObject, transition, fromNode, toNode);
		else if(match_page___PAGEKeyword_1_1_or_PageKeyword_1_0___LeftCurlyBracketKeyword_3_SemicolonKeyword_5_0_a.equals(transition.getAmbiguousSyntax()))
			emit_page___PAGEKeyword_1_1_or_PageKeyword_1_0___LeftCurlyBracketKeyword_3_SemicolonKeyword_5_0_a(semanticObject, transition, fromNode, toNode);
		else if(match_page___PAGEKeyword_1_1_or_PageKeyword_1_0___LeftCurlyBracketKeyword_3_SemicolonKeyword_5_0_p.equals(transition.getAmbiguousSyntax()))
			emit_page___PAGEKeyword_1_1_or_PageKeyword_1_0___LeftCurlyBracketKeyword_3_SemicolonKeyword_5_0_p(semanticObject, transition, fromNode, toNode);
		else if(match_selector_WSTerminalRuleCall_1_1_0_p.equals(transition.getAmbiguousSyntax()))
			emit_selector_WSTerminalRuleCall_1_1_0_p(semanticObject, transition, fromNode, toNode);
		else acceptNodes(transition, fromNode, toNode);
	}

	/**
	 * Syntax:
	 *     '@CHARSET' | '@charset'
	 */
	protected void emit_charset_CHARSETKeyword_0_1_or_CharsetKeyword_0_0(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		acceptNodes(transition, fromNode, toNode);
	}
	
	/**
	 * Syntax:
	 *     '-'?
	 */
	protected void emit_function_HyphenMinusKeyword_0_q(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		acceptNodes(transition, fromNode, toNode);
	}
	
	/**
	 * Syntax:
	 *     WS+
	 */
	protected void emit_function_WSTerminalRuleCall_3_p(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		acceptNodes(transition, fromNode, toNode);
	}
	
	/**
	 * Syntax:
	 *     WS*
	 */
	protected void emit_function_WSTerminalRuleCall_5_a(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		acceptNodes(transition, fromNode, toNode);
	}
	
	/**
	 * Syntax:
	 *     '@IMPORT' | '@import'
	 */
	protected void emit_importExpression_IMPORTKeyword_0_0_1_or_ImportKeyword_0_0_0(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		acceptNodes(transition, fromNode, toNode);
	}
	
	/**
	 * Syntax:
	 *     '@media' | '@MEDIA'
	 */
	protected void emit_media_MEDIAKeyword_0_1_or_MediaKeyword_0_0(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		acceptNodes(transition, fromNode, toNode);
	}
	
	/**
	 * Syntax:
	 *     '@page' | '@PAGE'
	 */
	protected void emit_page_PAGEKeyword_1_1_or_PageKeyword_1_0(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		acceptNodes(transition, fromNode, toNode);
	}
	
	/**
	 * Syntax:
	 *     ';'*
	 */
	protected void emit_page_SemicolonKeyword_5_0_a(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		acceptNodes(transition, fromNode, toNode);
	}
	
	/**
	 * Syntax:
	 *     ';'+
	 */
	protected void emit_page_SemicolonKeyword_5_0_p(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		acceptNodes(transition, fromNode, toNode);
	}
	
	/**
	 * Syntax:
	 *     ('@page' | '@PAGE') '{' ';'*
	 */
	protected void emit_page___PAGEKeyword_1_1_or_PageKeyword_1_0___LeftCurlyBracketKeyword_3_SemicolonKeyword_5_0_a(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		acceptNodes(transition, fromNode, toNode);
	}
	
	/**
	 * Syntax:
	 *     ('@page' | '@PAGE') '{' ';'+
	 */
	protected void emit_page___PAGEKeyword_1_1_or_PageKeyword_1_0___LeftCurlyBracketKeyword_3_SemicolonKeyword_5_0_p(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		acceptNodes(transition, fromNode, toNode);
	}
	
	/**
	 * Syntax:
	 *     WS+
	 */
	protected void emit_selector_WSTerminalRuleCall_1_1_0_p(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		acceptNodes(transition, fromNode, toNode);
	}
	
}
