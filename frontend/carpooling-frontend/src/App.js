import { Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Hero from "./components/Hero";
import Trajets from "./components/Trajets";
import Footer from "./components/Footer";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import MonComptePage from "./pages/MonComptePage";
import MonProfilPage from "./pages/MonProfilPage";

function App() {
    return (
        <div className="min-h-screen flex flex-col">
            <Navbar />
            <Routes>
                <Route
                    path="/"
                    element={
                        <>
                            <Hero />
                            <Trajets />
                        </>
                    }
                />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/mon-compte" element={<MonComptePage />} />
                <Route path="/profil" element={<MonProfilPage />} />
            </Routes>
            <Footer />
        </div>
    );
}

export default App;
