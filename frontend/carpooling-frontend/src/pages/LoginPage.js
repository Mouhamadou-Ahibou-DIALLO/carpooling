import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { login } from "../services/authService";

export default function LoginPage() {
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMsg, setErrorMsg] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMsg("");

        try {
            const user = await login(email, password);
            console.log("Utilisateur connecté ", user);

            window.dispatchEvent(new Event("userChanged"));

            navigate("/mon-compte");
        } catch (err) {
            console.log(err);

            if (!err.response) {
                setErrorMsg("Serveur injoignable !");
                return;
            }

            switch (err.response.status) {
                case 404:
                    setErrorMsg("Email inexistant !");
                    break;
                case 400:
                    setErrorMsg("Mot de passe incorrect !");
                    break;
                case 401:
                    setErrorMsg("Compte inactif, contactez l’admin !");
                    break;
                default:
                    setErrorMsg("Erreur inconnue, réessayez !");
            }
        }
    };

    return (
        <div className="flex flex-col items-center justify-center py-20 bg-gradient-to-b from-purple-50 to-white">
            <div className="bg-white shadow-lg rounded-xl p-8 w-80">
                <h2 className="text-2xl font-bold text-center text-purple-700 mb-6">
                    Connexion
                </h2>

                {errorMsg && (
                    <p className="text-red-500 text-center mb-3 font-medium">
                        {errorMsg}
                    </p>
                )}

                <form className="space-y-4" onSubmit={handleSubmit}>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Email</label>
                        <input
                            type="email"
                            className="w-full mt-1 p-2 border rounded-lg focus:ring-2 focus:ring-purple-500"
                            placeholder="exemple@email.com"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Mot de passe</label>
                        <input
                            type="password"
                            className="w-full mt-1 p-2 border rounded-lg focus:ring-2 focus:ring-purple-500"
                            placeholder="********"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-purple-600 text-white py-2 rounded-lg hover:bg-purple-700"
                    >
                        Se connecter
                    </button>
                </form>

                <p className="text-center text-sm text-gray-500 mt-4">
                    Nouveau ici ?{" "}
                    <Link to="/register" className="text-purple-600 hover:underline">
                        Créer un compte
                    </Link>
                </p>
            </div>
        </div>
    );
}
