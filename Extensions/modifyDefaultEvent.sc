+ Pattern {

	*modifyDefaultEvent { var p;

		p = Event.default.parent;

		p.note = #{ //add degContour to implement contour logic
			(~degree + ~mtranspose + ~degContour.value).degreeToKey(
				~scale,
				~scale.respondsTo(\stepsPerOctave).if(
					{ ~scale.stepsPerOctave },
					~stepsPerOctave)
			);
		};

		p.arp = #[0,2,4];
		p.diaOctave = 7;
		p.contour = 0;
		p.drag = 1;
		p.bass = 0;
		p.octave = 4;

		p.wrap = #{ arg a, deg, oct = 7;
			deg.wrapAt(a) + (oct*floor(a/deg.size))};

		p.firstWrap = #{ var s0;
			s0 = (floor(~bass/~diaOctave)+1)*(0-~arp.size);
			{ (~bass + ~wrap.value(s0,~arp,~diaOctave)) < ~arp[0] }.while({ s0=s0+1 });
			s0 };

		//degree is wrap_array + bass
		p.degContour = #{ ~arp.wrapAt(~contourCorr.value) +
			(~diaOctave*floor(~contourCorr.value/~arp.size)) + ~bass };

		p.contourCorr = #{ floor( ~firstWrap.value * ~drag ) + ~contour };
	}

}

