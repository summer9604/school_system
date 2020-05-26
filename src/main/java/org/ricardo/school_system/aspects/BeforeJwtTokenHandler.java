package org.ricardo.school_system.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BeforeJwtTokenHandler {

//	@Around("org.ricardo.school_system.aspects.PointCutDeclarations.validateToken()")
//	public void validateTokens(JoinPoint joinPoint) {
//		
//		HttpServletRequest request = null;
//		
//		for(Object arg : joinPoint.getArgs()) {
//			if(arg instanceof HttpServletRequest) {
//				request = (HttpServletRequest) arg;
//				break;
//			}
//		}
//		
//		HttpSession session = request.getSession(false);
//		
//		if (session == null || session.getAttribute("user-credentials") == null) {
//			return new ResponseEntity<>("Nothing to see here", HttpStatus.FORBIDDEN);
//		}		
//		
//		return null;
//	}
	
}
