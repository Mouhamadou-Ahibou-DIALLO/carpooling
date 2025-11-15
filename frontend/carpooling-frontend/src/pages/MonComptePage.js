import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getCurrentUser } from "../services/authService";

export default function MonComptePage() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);

    useEffect(() => {
        const loadUser = async () => {
            const data = await getCurrentUser();
            if (!data) {
                navigate("/login");
            }
            setUser(data);
        };
        loadUser();
    }, [navigate]);

    return (
        <div className="flex flex-col items-center justify-center flex-grow bg-gradient-to-b from-purple-50 to-white py-10">
            <div className="bg-white shadow-lg rounded-xl p-6 w-96 text-center">
                <h2 className="text-2xl font-bold text-purple-700 mb-4">
                    Bienvenue 👋
                </h2>

                {user ? (
                    <>
                        <p className="text-gray-700 text-lg">
                            <span className="font-semibold">Nom :</span> {user.username}
                        </p>
                        <p className="text-gray-700 text-lg">
                            <span className="font-semibold">Email :</span> {user.email}
                        </p>
                    </>
                ) : (
                    <p>Chargement...</p>
                )}
            </div>
        </div>
    );
}
