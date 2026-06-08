import { useState } from "react";
import axios from "axios";

const API_BASE = "http://localhost:8080";

const AIOpsAssistant = () => {

  const [open, setOpen] = useState(false);

  const [question, setQuestion] = useState("");

  const [loading, setLoading] = useState(false);

  const [messages, setMessages] = useState([
    {
      type: "ai",
      text: "Hello 👋 I'm your AI Ops Assistant. Ask me about health score, root cause, scaling, alerts or forecasts."
    }
  ]);

  const sendMessage = async () => {

    if (!question.trim()) {
      return;
    }

    const userMessage = {
      type: "user",
      text: question
    };

    setMessages(prev => [...prev, userMessage]);

    const currentQuestion = question;

    setQuestion("");

    try {

      setLoading(true);

      const response = await axios.post(
        `${API_BASE}/api/chat`,
        {
          question: currentQuestion
        }
      );

      setMessages(prev => [
        ...prev,
        {
          type: "ai",
          text: response.data.answer
        }
      ]);

    } catch (error) {

      console.error(error);

      setMessages(prev => [
        ...prev,
        {
          type: "ai",
          text: "Unable to get response from AI service."
        }
      ]);

    } finally {

      setLoading(false);

    }
  };

  return (
    <>
      {/* Floating Button */}

      <button
        onClick={() => setOpen(!open)}
        style={{
          position: "fixed",
          bottom: "30px",
          right: "30px",
          width: "65px",
          height: "65px",
          borderRadius: "50%",
          border: "none",
          cursor: "pointer",
          fontSize: "28px",
          zIndex: 9999,
          background:
            "linear-gradient(135deg,#2563eb,#7c3aed)",
          color: "white",
          boxShadow:
            "0 0 25px rgba(99,102,241,0.5)"
        }}
      >
        🤖
      </button>

      {/* Chat Window */}

      {open && (
        <div
          style={{
            position: "fixed",
            bottom: "110px",
            right: "30px",
            width: "420px",
            height: "600px",
            background: "#0f172a",
            border: "1px solid #334155",
            borderRadius: "20px",
            display: "flex",
            flexDirection: "column",
            zIndex: 9999,
            overflow: "hidden",
            boxShadow:
              "0 0 30px rgba(59,130,246,0.3)"
          }}
        >
          {/* Header */}

          <div
            style={{
              padding: "15px",
              background:
                "linear-gradient(90deg,#2563eb,#7c3aed)",
              color: "white",
              fontWeight: "bold",
              fontSize: "18px"
            }}
          >
            AI Ops Assistant
          </div>

          {/* Messages */}

          <div
            style={{
              flex: 1,
              overflowY: "auto",
              padding: "15px"
            }}
          >
            {messages.map((message, index) => (

              <div
                key={index}
                style={{
                  display: "flex",
                  justifyContent:
                    message.type === "user"
                      ? "flex-end"
                      : "flex-start",
                  marginBottom: "12px"
                }}
              >
                <div
                  style={{
                    maxWidth: "80%",
                    padding: "12px",
                    borderRadius: "12px",
                    background:
                      message.type === "user"
                        ? "#2563eb"
                        : "#1e293b",
                    color: "white",
                    whiteSpace: "pre-wrap"
                  }}
                >
                  {message.text}
                </div>
              </div>

            ))}

            {loading && (

              <div
                style={{
                  color: "#94a3b8",
                  fontStyle: "italic"
                }}
              >
                AI is thinking...
              </div>

            )}
          </div>

          {/* Input */}

          <div
            style={{
              padding: "15px",
              borderTop:
                "1px solid rgba(255,255,255,0.1)"
            }}
          >
            <div
              style={{
                display: "flex",
                gap: "10px"
              }}
            >
              <input
                type="text"
                value={question}
                placeholder="Ask about cluster health..."
                onChange={(e) =>
                  setQuestion(e.target.value)
                }
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    sendMessage();
                  }
                }}
                style={{
                  flex: 1,
                  padding: "10px",
                  borderRadius: "8px",
                  border: "1px solid #334155",
                  background: "#1e293b",
                  color: "white"
                }}
              />

              <button
                onClick={sendMessage}
                disabled={loading}
                style={{
                  padding:
                    "10px 18px",
                  borderRadius: "8px",
                  border: "none",
                  background: "#2563eb",
                  color: "white",
                  cursor: "pointer"
                }}
              >
                Send
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default AIOpsAssistant;