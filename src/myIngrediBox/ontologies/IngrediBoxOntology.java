package myIngrediBox.ontologies;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

public class IngrediBoxOntology extends BeanOntology
{
	
	  private static final long serialVersionUID = 1L;
	  
	  /**
	   * name used in ACL-Messages
	   */
	  public static String ONTOLOGY_NAME = "IngrediBox-ontology";
	   
	  /**
	   * singleton pattern
	   */
	  private static Ontology instance = new IngrediBoxOntology( IngrediBoxOntology.ONTOLOGY_NAME );
	
	  
	  private IngrediBoxOntology(String name, Ontology base)
	  {
	    super(name, base);
	    registerOntologicClasses();
	  }
	
	  private IngrediBoxOntology(String name, Ontology[] base) 
	  {
	    super(name, base);
	    registerOntologicClasses();
	  }	
	
	 
	  private IngrediBoxOntology(String name)
	  {
	    super(name);
	    registerOntologicClasses();
	  }
	  
	  /**
	   * get all Classes that implement ontological interfaces
	   */
	  private void registerOntologicClasses() 
	  {
	    try
	    {
	      this.add( "myIngrediBox.ontologies" );
	    }
	    catch( BeanOntologyException boe )
	    {
	      boe.printStackTrace();
	    }
	  }
	  
	  /**
	   * typically used as singleton access for ontology 
	   * @return
	   */
	  public static Ontology getInstance()
	  {
	    return instance;
	  }

}
