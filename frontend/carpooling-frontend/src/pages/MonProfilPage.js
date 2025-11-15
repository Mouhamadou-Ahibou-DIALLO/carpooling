import { useEffect, useState } from "react";
import { getCurrentUser } from "../services/authService";
import { completeUser, updateUser, deleteUser } from "../services/userService";
import { useNavigate } from "react-router-dom";

export default function MonProfilPage() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [showDeletePopup, setShowDeletePopup] = useState(false);

    useEffect(() => {
        const loadUser = async () => {
            const data = await getCurrentUser();
            if (!data) return navigate("/login");
            setUser(data);
            setLoading(false);
        };
        loadUser();
    }, [navigate]);

    if (loading) return <p className="text-center py-10">Chargement...</p>;

    const isProfileComplete =
        user.isVerified &&
        user.photoUser &&
        user.address &&
        user.roleUser;

    const handleCompleteSubmit = async (e) => {
        e.preventDefault();

        const formData = {
            photoUser: e.target.photoUser.value,
            address: e.target.address.value,
            roleUser:
                e.target.roleUser.value === "PASSENGER"
                    ? "ROLE_PASSENGER"
                    : "ROLE_DRIVER"
        };

        await completeUser(formData);
        window.location.reload();
    };

    const handleUpdateSubmit = async (e) => {
        e.preventDefault();

        const formData = {
            username: e.target.username.value,
            email: e.target.email.value,
            password: e.target.password.value || null,
            phoneNumber: e.target.phoneNumber.value,
            photoUser: e.target.photoUser.value,
            address: e.target.address.value,
            roleUser: user.roleUser
        };

        await updateUser(formData);
        window.location.reload();
    };

    const confirmDelete = async () => {
        await deleteUser();
        navigate("/login");
    };

    return (
        <div className="container mx-auto py-10 px-4 max-w-2xl">

            <div className="bg-white shadow-lg rounded-xl p-6 mb-8">
                <h2 className="text-2xl font-bold text-purple-700 mb-4">Mon Profil</h2>

                <div className="text-center mb-6">
                    {user.photoUser ? (
                        <img
                            src={user.photoUser}
                            alt="Profil"
                            className="w-32 h-32 mx-auto rounded-full shadow"
                        />
                    ) : (
                        <div className="w-32 h-32 mx-auto rounded-full bg-gray-200 flex items-center justify-center text-gray-500">
                            Pas de photo
                        </div>
                    )}
                </div>

                <p><strong>Nom :</strong> {user.username}</p>
                <p><strong>Email :</strong> {user.email}</p>
                <p><strong>Téléphone :</strong> {user.phoneNumber || "Non renseigné"}</p>
                <p><strong>Adresse :</strong> {user.address || "Non renseignée"}</p>
                <p><strong>Rôle :</strong> {user.roleUser}</p>
                <p><strong>Vérifié :</strong> {user.isVerified ? "Oui" : "Non"}</p>
            </div>

            {!isProfileComplete && (
                <div className="bg-white shadow-xl rounded-xl p-6">
                    <h3 className="text-xl font-semibold mb-4 text-blue-600">
                        Compléter mon profil
                    </h3>

                    <form onSubmit={handleCompleteSubmit} className="space-y-4">
                        <input name="photoUser" required placeholder="URL de la photo"
                               className="w-full p-2 border rounded" />

                        <input name="address" required placeholder="Adresse"
                               className="w-full p-2 border rounded" />

                        <select name="roleUser" required className="w-full p-2 border rounded">
                            <option value="">Sélectionner un rôle</option>
                            <option value="PASSENGER">PASSAGER</option>
                            <option value="DRIVER">CONDUCTEUR</option>
                        </select>

                        <button
                            type="submit"
                            className="w-full bg-purple-600 text-white py-2 rounded hover:bg-purple-700"
                        >
                            Valider
                        </button>
                    </form>
                </div>
            )}

            {isProfileComplete && (
                <div className="bg-white shadow-xl rounded-xl p-6 mt-8">
                    <h3 className="text-xl font-semibold mb-4 text-green-600">
                        Modifier mon profil
                    </h3>

                    <form onSubmit={handleUpdateSubmit} className="space-y-4">
                        <input name="username" defaultValue={user.username}
                               className="w-full p-2 border rounded" />

                        <input name="email" defaultValue={user.email}
                               className="w-full p-2 border rounded" />

                        <input name="password" type="password" placeholder="Nouveau mot de passe"
                               className="w-full p-2 border rounded" />

                        <input name="phoneNumber" defaultValue={user.phoneNumber}
                               className="w-full p-2 border rounded" />

                        <input name="photoUser" defaultValue={user.photoUser}
                               className="w-full p-2 border rounded" />

                        <input name="address" defaultValue={user.address}
                               className="w-full p-2 border rounded" />

                        <button
                            type="submit"
                            className="w-full bg-green-600 text-white py-2 rounded hover:bg-green-700"
                        >
                            Mettre à jour
                        </button>
                    </form>

                    <button
                        onClick={() => setShowDeletePopup(true)}
                        className="mt-6 w-full bg-red-600 text-white py-2 rounded hover:bg-red-700"
                    >
                        Supprimer mon compte
                    </button>
                </div>
            )}

            {showDeletePopup && (
                <div className="fixed inset-0 bg-black/50 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-xl shadow-xl max-w-sm text-center">
                        <h3 className="text-lg font-semibold mb-4 text-red-600">
                            Confirmer la suppression ?
                        </h3>
                        <p className="mb-6">Cette action est irréversible.</p>

                        <button
                            className="bg-red-600 text-white px-4 py-2 rounded mr-3"
                            onClick={confirmDelete}
                        >
                            Supprimer
                        </button>

                        <button
                            className="bg-gray-300 px-4 py-2 rounded"
                            onClick={() => setShowDeletePopup(false)}
                        >
                            Annuler
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}
