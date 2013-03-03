+ Pattern {

	//gkc: make a "play" that tracks the playing streams in ~track
	track {
		if( ~track.isNil, {~track=List()} ); //init ~track
		~p = this.play( TempoClock.default, quant:1 );
		~track.add( ~p );

		//~v = EZPopUpMenu();
		//~v.addItem( \play, {~p.play(quant:1)} );
		//~v.addItem( \stop, {~p.stop} );


	}

	//this is for looking up keys in event patterns
	at {
		arg key;
		^this.collect{|e|e[key]};
	}

	//some synonyms for differentiate
	diff {
		^this.differentiate;
	}

	//and for jamshark's Paccum
	accum {
		arg startArg=0;
		^Paccum( start: startArg, step: this );
	}

	//this is for doing instantaneous modifications on props??

	//synonym for collect
	map {
		arg func;
		^this.collect(func);
	}

	//now make a stream where we map or remap an event parameter
	mapAt {
		arg key, func;
		^Pbindf( this, key, this.at(key).map(func) );
	}

	//easy chaining with mono
	mono {
		arg inst=\default;
		^this<>Pmono(inst)
	}

	//adding new keys with Pbindf
	//bind {
	//	arg ... pkeys;
	//	^Pbindf( this, pkeys );
	//}

	//some synonyms- is there a way to get this behavior automagically?
	//maybe by modifying at?
	pan { arg p; ^Pbindf( this, \pan, p ); }
	inst { arg i; ^Pbindf( this, \instrument, i ); }
	scale { arg s; ^Pbindf( this, \scale, s ); }
	degree { arg d; ^Pbindf( this, \degree, d ); }

	//random pans, random pan initial
	rpani { ^this.pan( 1.0.rand2 ) }
	rpan { ^this.pan( Pwhite(-1,1) ) }

	bindf {
		arg estream, key;
		^Pbindf( estream, key, this );
	}

	//try to count streams automatically
	//classvar streamCount;

	*initClass {
		StreamCounter.initClass;
	}

	//replace the play method
	go { arg quant = 1;
		^Pbindf( this, \snum, StreamCounter.counter.value ).play(quant:quant);
	}
}