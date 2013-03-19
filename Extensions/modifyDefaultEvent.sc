+ Pattern {

	*modifyDefaultEvent { var p;

		~streamNum = 0;

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
		p.snum = 0; //stream number

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

		p.playNormal = #{
			var tempo, server;

			~finish.value;

			server = ~server ?? { Server.default };

			tempo = ~tempo;
			if (tempo.notNil) {
				thisThread.clock.tempo = tempo;
			};
			// ~isRest may be nil - force Boolean behavior
			if(~isRest != true) { ~eventTypes[~type].value(server) };
		};

		p.playPost = #{ var n;
			//do the actions after all the note work
			//n = NetAddr("tamats.com", 4344);
			n = NetAddr("localhost", 3333);
			n.sendMsg("/midi", ~note.value, ~snum);

			//~note.value.post; ' '.post; ~snum.postln;
		};

		p.play = #{ ~playNormal.value();
			~playPost.value();
		};
	}

}

//+ Pbind {

/*	*new { arg ... pairs;
		if (pairs.size.odd, { Error("Pbind should have even number of args.\n").throw; });
		if( topEnvironment[\snum].isNil ) { topEnvironment[\snum]=0 } { topEnvironment[\snum]=topEnvironment[\snum]+1 };
		^super.newCopyArgs(\snum, topEnvironment[\snum], pairs)
	}*/

//}

/*PbindSuper {

	*new { arg ... pairs;
		^super.new( pairs ); }

}*/

+ Array {

	pseq { ^Pseq( this, inf ) }

}

+ Buffer {

	*adequate { var s,b; s = Server.default;
		b = Buffer.alloc( s, s.sampleRate * 5, 2 );
	    ^b }

	*aok { ^Buffer.adequate }

}

