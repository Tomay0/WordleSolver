import React, {ReactElement} from 'react';
import './App.css';

function Tile(props:{letter: string, guessState: number, onClick: () => void}) {
  return (
    <button onClick={props.onClick} className={`tile state_${props.guessState}`}>
      {props.letter}
    </button>
  );
}

export default class TileRow extends React.Component<{word: string}, {guessState: Array<number>}> {
  constructor(props: {word: string}) {
    super(props);
    this.changeGuessState = this.changeGuessState.bind(this);

    this.state = {guessState: [0,0,0,0,0]};
  }

  changeGuessState(i: number) {
    const guessState = this.state.guessState;
    guessState[i] = (guessState[i] + 1) % 3;

    this.setState({guessState});
  }

  render() {
    return (
      <div className="tile-row">
        {Array.from(Array(5).keys()).map((i) => (<Tile letter={this.props.word[i]} guessState={this.state.guessState[i]} key={i} onClick={() => this.changeGuessState(i)} />))}
      </div>
    );
  }
}