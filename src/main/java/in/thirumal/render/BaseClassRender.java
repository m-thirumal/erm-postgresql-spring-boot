/**
 * 
 */
package in.thirumal.render;

import in.thirumal.model.Entity;
import lombok.Getter;

/**
 * @author thirumal
 *
 */
@Getter
public abstract class BaseClassRender {
	
	private Entity entity;
	
	public	BaseClassRender(Entity entity){
		this.entity = entity;
	}
	
	public abstract String render() throws Exception;
}
