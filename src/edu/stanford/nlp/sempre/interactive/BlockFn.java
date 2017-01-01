package edu.stanford.nlp.sempre.interactive;

import java.util.*;
import java.util.stream.Collectors;

import org.testng.collections.Lists;

import com.google.common.collect.ImmutableList;

import edu.stanford.nlp.sempre.*;
import edu.stanford.nlp.sempre.SemanticFn.Callable;
import edu.stanford.nlp.sempre.interactive.actions.ActionFormula;
import fig.basic.LispTree;
import fig.basic.MapUtils;
import fig.basic.Option;

/**
 * return a block sequencing all the children
 *
 * @author sidaw
 */
public class BlockFn extends SemanticFn {
  public static class Options {
    @Option(gloss = "verbosity") public int verbose = 0;
  }
  public static Options opts = new Options();

  
  ActionFormula.Mode mode = ActionFormula.Mode.block;

  public void init(LispTree tree) {
    super.init(tree);
    if (tree.child(1).value.equals("sequential")) mode = ActionFormula.Mode.sequential;
  }

  public BlockFn() {}

  public DerivationStream call(final Example ex, final Callable c) {
    return new SingleDerivationStream() {
      @Override
      public Derivation createDerivation() {
        List<Derivation> args = c.getChildren();
        
        Formula f = new ActionFormula(mode, 
            args.stream().map(d -> d.formula).collect(Collectors.toList()));
        Derivation res = new Derivation.Builder()
            .formula(f)
            .withCallable(c)
            .createDerivation();
        
        return res;
      }
    };
  }
}