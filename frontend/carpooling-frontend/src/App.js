import Navbar from "./components/Navbar";
import Hero from "./components/Hero";
import Trajets from "./components/Trajets";
import Footer from "./components/Footer";

export default function App() {
    return (
        <div className="min-h-screen flex flex-col">
            <Navbar />
            <Hero />
            <Trajets />
            <Footer />
        </div>
    );
}
