import { useState } from 'react'
import './App.css'

function App() {
  const [playerCount, setPlayerCount] = useState(1)

  function addPlayer() {
    setPlayerCount(currentCount => Math.min(4, currentCount + 1))

  }
  function removePlayer() {

    setPlayerCount(currentCount => Math.max(0, currentCount + -1))

  }
  return (
      <main>
        <h1>Whatever Game</h1>

        <p>当前房间人数：{playerCount}</p>

        <button onClick={addPlayer} disabled={playerCount >= 4}>
          加入一名玩家
        </button>
        <button onClick={removePlayer} disabled={playerCount <= 0}>
          离开一名玩家
        </button>
        {playerCount >= 4 && <p>房间已满</p>}
      </main>
  )
}

export default App