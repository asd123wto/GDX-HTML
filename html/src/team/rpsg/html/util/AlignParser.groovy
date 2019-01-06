package team.rpsg.html.util

import com.badlogic.gdx.utils.Align
import groovy.transform.CompileStatic

@CompileStatic
class AlignParser {
	static int textAlign(Object align, int defaultAlign = Align.left){
		if(!align || align.toString().length() == 0)
			return defaultAlign

		def s = align.toString()

		switch (s.toLowerCase()){
			case "left": return Align.left
			case "right": return Align.right
			case "center": return Align.center
		}

		return defaultAlign
	}

	static int join(int lr, int tb){
		if(lr == Align.center)
			return tb

		return tb | lr
	}
}