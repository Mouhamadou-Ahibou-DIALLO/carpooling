import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { logout, getCurrentUser } from "../services/authService";

export default function Navbar() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);

    const loadUser = async () => {
        const u = await getCurrentUser();
        setUser(u);
    };

    useEffect(() => {
        loadUser();

        const handler = () => loadUser();
        window.addEventListener("userChanged", handler);

        return () => {
            window.removeEventListener("userChanged", handler);
        };
    }, []);

    const handleLogout = async () => {
        await logout();
        setUser(null);
        window.dispatchEvent(new Event("userChanged"));
        navigate("/login");
    };

    return (
        <nav className="flex items-center justify-between px-8 py-4 bg-white shadow">
            <div className="flex items-center space-x-2">
                <Link to="/" className="text-2xl font-bold text-purple-600">
                    🚗 Carpooling
                </Link>
            </div>

            <div className="flex items-center space-x-6">
                <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
                    Rechercher
                </button>
                <a href="#" className="text-gray-700 hover:text-blue-600">Publier un trajet</a>
                <a href="#" className="text-gray-700 hover:text-blue-600">Mes trajets</a>
            </div>

            <div>
                {user ? (
                    <div className="flex items-center space-x-3">
                        <Link to="/profil" className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-100">
                            Mon Profil
                        </Link>
                        <button
                            onClick={handleLogout}
                            className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600"
                        >
                            Déconnexion
                        </button>
                    </div>
                ) : (
                    <Link
                        to="/login"
                        className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-100"
                    >
                        👤 Mon compte
                    </Link>
                )}
            </div>
        </nav>
    );
}
